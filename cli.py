import sys
import requests

r = requests.get('http://localhost:4567/date_start/%s/date_end/%s' % (sys.argv[1], sys.argv[2]))
print(r.json())
