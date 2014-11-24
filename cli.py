from cli import *

nd = NcursesDisplay()
nd.display()

"""
if __name__ == '__main__':
    # if len(sys.argv) < 3:
    #     print("Usage: python cli.py <start> <end>")
    #     sys.exit(1)

    # events = get_events(0, datetime_to_seconds_since_epoch(datetime.datetime.now()))
    events = get_events_file("json.txt")
    c = CursesView()
    try:
        c.display_daily(events)
    except Exception as e:
        c.cleanup()
        print(e)
        traceback.print_exc()
    finally:
        c.cleanup()

    # events = get_events(sys.argv[1], sys.argv[2])
    display_daily_term(events)
"""