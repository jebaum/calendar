import sys, json
import requests

class Event(object):
    def __init__(self):
        self.title = None
        self.startTime = 0
        self.endTime = 0
        self.location = None
        self.category = None
        self.description = None
        self.json_keys = ["title", "startTime", "endTime", "location", "category", "description"]

    def __repr__(self):
        return self.__str__()

    def __str__(self):
        json_dict = {}
        for k in self.json_keys:
            json_dict[k] = getattr(self, k)

        return json.dumps(json_dict)

    def load_from_json(self, json_dict):
        for k,v in json_dict.items():
            setattr(self, k, v)


r = requests.get('http://localhost:4567/date_start/%s/date_end/%s' % (sys.argv[1], sys.argv[2]))
for item in r.json():
    e = Event()
    e.load_from_json(item)
    print(e)
