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
    def startMinute(self):
        return self.startDate.minute

    @property
    def startHour(self):
        return self.startDate.hour

    @property
    def startDay(self):
        return self.startDate.day

    @property
    def startMonth(self):
        return self.startDate.month

    @property
    def startYear(self):
        return self.startDate.year

    @property
    def endMinute(self):
        return self.endDate.minute

    @property
    def endHour(self):
        return self.endDate.hour

    @property
    def endDay(self):
        return self.endDate.day

    @property
    def endMonth(self):
        return self.endDate.month

    @property
    def endYear(self):
        return self.endDate.year

    def load_from_json(self, json_dict):
        for k,v in json_dict.items():
            setattr(self, k, v)

        # Create datetime objects from seconds from epoch
        # Must convert miliseconds since epoch (standard of api) to seconds first
        self.startDate = datetime.datetime.fromtimestamp(self.startTime/1000)
        self.endDate = datetime.datetime.fromtimestamp(self.endTime/1000)
