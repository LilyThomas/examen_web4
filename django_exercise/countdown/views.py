from django.shortcuts import render

import datetime;

# Create your views here.

def index(request):
    now = datetime.datetime.now();

    # be is 1 uur later
    timediff = 1

    # uk local
    brexit = datetime.datetime(2019, 3, 29, 23)

    if(brexit.hour + timediff == 24):
        countdown = datetime.datetime(2019,3,30)
    else:
        coundown = brexit

    diff = countdown - now

    return render(request, 'countdown/index.html', {'timediff': timediff, 'date': diff, 'uk': brexit, 'now': now})

