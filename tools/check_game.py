#!/usr/bin/env python3
"""Compile the legacy core independently of Android Gradle; optionally render desktop smoke checks."""
import argparse
import concurrent.futures
import os
from pathlib import Path
import re
import subprocess
import tempfile
import urllib.request
import wave

ROOT = Path(__file__).resolve().parents[1]
GAME = ROOT / 'game - Android Studio'
CACHE = Path((os.environ.get('CRAZYBASKET_CHECK_TMP') or tempfile.gettempdir())) / 'crazybasket-check-1.9.10'
MAVEN = 'https://repo.maven.apache.org/maven2/'
DEPENDENCIES = {
    'gdx.jar': 'com/badlogicgames/gdx/gdx/1.9.10/gdx-1.9.10.jar',
    'box2d.jar': 'com/badlogicgames/gdx/gdx-box2d/1.9.10/gdx-box2d-1.9.10.jar',
}
DESKTOP = {
    'backend.jar': 'com/badlogicgames/gdx/gdx-backend-lwjgl/1.9.10/gdx-backend-lwjgl-1.9.10.jar',
    'lwjgl.jar': 'org/lwjgl/lwjgl/lwjgl/2.9.2/lwjgl-2.9.2.jar',
    'lwjgl-util.jar': 'org/lwjgl/lwjgl/lwjgl_util/2.9.2/lwjgl_util-2.9.2.jar',
    'lwjgl-natives.jar': 'org/lwjgl/lwjgl/lwjgl-platform/2.9.2/lwjgl-platform-2.9.2-natives-linux.jar',
    'gdx-natives.jar': 'com/badlogicgames/gdx/gdx-platform/1.9.10/gdx-platform-1.9.10-natives-desktop.jar',
    'box2d-natives.jar': 'com/badlogicgames/gdx/gdx-box2d-platform/1.9.10/gdx-box2d-platform-1.9.10-natives-desktop.jar',
    'jlayer.jar': 'com/badlogicgames/jlayer/jlayer/1.0.1-gdx/jlayer-1.0.1-gdx.jar',
    'jorbis.jar': 'org/jcraft/jorbis/0.0.17/jorbis-0.0.17.jar',
}

def fetch(item):
    name, url = item
    target = CACHE / name
    if not target.exists():
        urllib.request.urlretrieve(MAVEN + url, target)
    return target

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--render', action='store_true', help='Requires an X display and OpenGL; saves actual game screenshots.')
    args = parser.parse_args()
    CACHE.mkdir(exist_ok=True)
    dependencies = {**DEPENDENCIES, **(DESKTOP if args.render else {})}
    with concurrent.futures.ThreadPoolExecutor(max_workers=6) as pool:
        list(pool.map(fetch, dependencies.items()))
    classpath = os.pathsep.join([str(CACHE / name) for name in dependencies] + [str(GAME / 'libs/tween-engine-api.jar')])
    sources = list((GAME / 'core/src').rglob('*.java')) + [ROOT / 'tests/GameRulesCheck.java']
    if args.render:
        sources.append(ROOT / 'tests/DesktopSmokeCheck.java')
    classes = CACHE / 'classes'
    classes.mkdir(exist_ok=True)
    subprocess.run(['javac', '-encoding', 'UTF-8', '--release', '8', '-cp', classpath, '-d', str(classes), *map(str, sources)], check=True)
    classpath += os.pathsep + str(classes)
    subprocess.run(['java', '-cp', classpath, 'GameRulesCheck'], check=True)
    assets = GAME / 'android/assets'
    glyphs = set(map(int, re.findall(r'char id=(\d+)', (assets / 'misc/localized-font.fnt').read_text())))
    strings = re.findall(r'"([^"\n]*)"', (GAME / 'core/src/configuration/Localization.java').read_text())
    missing = {c for text in strings for c in text if ord(c) not in glyphs}
    assert not missing, f'Missing font glyphs: {missing}'
    with wave.open(str(assets / 'sounds/good.wav')) as voice:
        assert voice.getnchannels() == 1 and voice.getsampwidth() == 2
        assert 0.2 < voice.getnframes() / voice.getframerate() < 2
        assert any(voice.readframes(voice.getnframes())), 'Empty voice recording'
    for name in ['crazy-basket-logo.png', 'good-sticker.png', 'audela-logo.png']:
        assert (assets / name).is_file(), f'Missing branding asset: {name}'
    print('PASS: complete font coverage, voice WAV and branding assets')
    if args.render:
        output = ROOT / 'output/previews'
        output.mkdir(parents=True, exist_ok=True)
        subprocess.run(['java', '-cp', classpath, 'DesktopSmokeCheck', str(output)], cwd=assets, check=True)

if __name__ == '__main__':
    main()
