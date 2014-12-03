import json, datetime

class Event(object):
    def __init__(self):
        self.title = None
        self.startTime = 0
        self.endTime = 0
        self.location = None
        self.category = None
        self.description = None
        self.json_keys = ["title", "startTime", "endTime", "location", "category", "description"]
        
        self.startDate = datetime.datetime.fromtimestamp(self.startTime/1000)
        self.endDate = datetime.datetime.fromtimestamp(self.endTime/1000)

    def __repr__(self):
        return self.__str__()

    def __str__(self):
        json_dict = {}
        for k in self.json_keys:
            json_dict[k] = getattr(self, k)

        return json.dumps(json_dict, sort_keys=True, indent=4, separators=(',', ': '))

    @property
    def minute(self):
        return self.startDate.minute

    @property
    def hour(self):
        return self.startDate.hour

    @property
    def day(self):
        return self.startDate.day

    @property
    def month(self):
        return self.startDate.month

    @property
    def year(self):
        return self.startDate.year

    def load_from_json(self, json_dict):
        for k,v in json_dict.items():
            setattr(self, k, v)

        # Create datetime objects from seconds from epoch
        # Must convert miliseconds since epoch (standard of api) to seconds first
        self.startDate = datetime.datetime.fromtimestamp(self.startTime/1000)
        self.endDate = datetime.datetime.fromtimestamp(self.endTime/1000)
