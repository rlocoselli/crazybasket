#!/usr/bin/env python3
"""Serve the signed test APK and refresh its QR when the LAN address changes."""
import argparse, functools, http.server, io, json, os, socket, threading
from pathlib import Path
import qrcode
ROOT=Path(__file__).resolve().parents[1]
DOWNLOADS=ROOT/'output/test-download'
APK='crazy-basket-1.2.0.apk'
def write_existing(path,data):
    fd=os.open(path,os.O_WRONLY|os.O_CREAT,0o644)
    try:
        view=memoryview(data)
        while view:
            count=os.write(fd,view);view=view[count:]
        os.ftruncate(fd,len(data))
    finally:os.close(fd)
def lan_address():
    with socket.socket(socket.AF_INET,socket.SOCK_DGRAM) as route:
        route.connect(('1.1.1.1',80));return route.getsockname()[0]
def main():
    parser=argparse.ArgumentParser()
    parser.add_argument('--host',default='0.0.0.0')
    parser.add_argument('--advertise-host')
    parser.add_argument('--port',type=int,default=8765)
    args=parser.parse_args()
    if not (DOWNLOADS/APK).is_file():parser.error('Signed test APK missing.')
    http.server.SimpleHTTPRequestHandler.extensions_map['.apk']='application/vnd.android.package-archive'
    handler=functools.partial(http.server.SimpleHTTPRequestHandler,directory=str(DOWNLOADS))
    stop=threading.Event();current=[None]
    def refresh():
        url=f'http://{args.advertise_host or lan_address()}:{args.port}/{APK}'
        if url==current[0]:return
        qr=qrcode.QRCode(error_correction=qrcode.constants.ERROR_CORRECT_M,box_size=10,border=4)
        qr.add_data(url);qr.make(fit=True)
        buffer=io.BytesIO();qr.make_image(fill_color='black',back_color='white').save(buffer,format='PNG')
        write_existing(DOWNLOADS/'crazy-basket-qr.png',buffer.getvalue())
        write_existing(ROOT/'output/test-download-url.txt',(url+'\n').encode())
        path=DOWNLOADS/'release-metadata.json'
        metadata=json.loads(path.read_text());metadata['download_url']=url
        write_existing(path,(json.dumps(metadata,indent=2)+'\n').encode())
        current[0]=url;print(url,flush=True)
    def monitor():
        while not stop.wait(5):
            try:refresh()
            except OSError:pass
    with http.server.ThreadingHTTPServer((args.host,args.port),handler) as server:
        refresh();threading.Thread(target=monitor,daemon=True).start()
        try:server.serve_forever()
        except KeyboardInterrupt:pass
        finally:stop.set()
if __name__=='__main__':main()
