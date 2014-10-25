UCLA CS130 Calendaring + Task Management Application
Fall 2014

Usage
=====
- Requires apache-ant and apache-ivy for building and fetching dependencies
- Run `ant bootstrap` one time only to download ivy and other needed jars
- Ant `build`, `run`, and `clean` targets do exactly what they sound like

Components
==========
- Backend: James
- Web UI: Braden
- Terminal/curses UI: Seena
- Android app/UI: Kevin

API
===
- GET /?date_start=123&date_end=234
```
  [
    {
      name: "Cool event",
      start: 1414020841,
      end: 1414020841
    },
    {
      ...
    },
    ...
  ]
```
- POST /
```
  [
    {
      name: "Cool event",
      start: 1414020841,
      end: 1414020841
    },
    {
      ...
    },
    ...
  ]
```
- PATCH /<id>, in memory
- DELETE /<id>, in memory
