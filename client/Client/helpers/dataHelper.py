import urllib2, json, time
import subprocess
import os

from psutil import process_iter
from signal import SIGTERM # or SIGKILL
from cluster import Cluster

	
def make_get_request(ip, path=None, args=None):
    url = ip + "/"

    if path:
        url += path

    if args:
        url += "/".join(args)

    try:
        r = urllib2.urlopen(url)
    except urllib2.URLError, e:
        print url
        print e.reason

    data = json.loads(r.read())
    return data

def make_post_request(ip, path=None, body=None):
    url = ip + "/"

    if path:
        url += path

    if args:
        url += "/".join(args)

    try:
        req = urllib2.Request(url=url,data=body)
        r = urllib2.urlopen(req)
    except urllib2.URLError, e:
        print url
        print e.reason

    data = json.loads(r.read())
    return data

