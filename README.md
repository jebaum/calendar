UCLA CS130 Calendaring + Task Management Application
Fall 2014


Components
==========
- Backend: James
- Web UI: Braden
- Terminal/curses UI: Seena
- Android app/UI: Kevin

API
===
- GET /?date_range={start:1414020841, end:1414020841}
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
- PATCH /<hash> 
- DELETE /<hash> 
