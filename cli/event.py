import datetime

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

    def clamp_seconds_since_epoch(self, sec):
        sec_string = str(sec)
        if len(sec_string) > 10:
            return int(sec_string[:10])
        elif len(sec_string) < 10:
            return int(sec_string.ljust(10, '0'))
        return sec_string

    def load_from_json(self, json_dict):
        for k,v in json_dict.items():
            setattr(self, k, v)

        # Adjust startTime / endTime to ensure in seconds since epoch
        self.startTime = self.clamp_seconds_since_epoch(self.startTime)
        self.endTime = self.clamp_seconds_since_epoch(self.endTime)

        self.startDate = datetime.datetime.fromtimestamp(self.startTime)
        self.endDate = datetime.datetime.fromtimestamp(self.endTime)
