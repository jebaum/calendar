UCLA CS130 Calendaring + Task Management Application
Fall 2014

Components
==========
- Backend: James
- Web UI: Braden
- Terminal/curses UI: Seena
- Android app/UI: Kevin

Usage
=====
- Requires apache-ant and apache-ivy for building and fetching dependencies
- Run `ant bootstrap` one time only to download ivy and other needed jars
- Ant `build`, `run`, and `clean` targets do exactly what they sound like
- Webserver currently listens on port `4567`
- Requires Java 8

API
===
#### 1. Get event data via HTTP GET
- `GET /date_start/UNIXTIMESTAMP/date_end/UNIXTIMESTAMP`
- Go to `http://localhost:4567/date_start/123/date_end/234` in browser, or
- `curl 'localhost:4567/date_start/123/date_end/234'`
- Sample response:
```
[
  {
    "title"       : "this is my title",
    "location"    : "here i am",
    "description" : "sup",
    "category"    : "important",
    "startTime"   : 1414000000000,
    "endTime"     : 1414005000000
  },
  {
    "title"       : "another title",
    "location"    : "nowhere",
    "description" : "things are weird",
    "category"    : "unimportant",
    "startTime"   : 1414010000000,
    "endTime"     : 1414013000000
  }
]
```
#### 2. Send event data via HTTP POST
- POST /
- Example usage:
```
curl --data '[{"title":"lecture","location":"a room","description":"boring","category":"lame","startTime":1414006000000,"endTime":1414009000000}]' 'localhost:4567'
```

#### 3. Update events with HTTP PATCH
- PATCH /<id>, in memory
- Work in progress

#### 4. Delete events with HTTP DELETE
- DELETE /<id>, in memory
- Work in progress
