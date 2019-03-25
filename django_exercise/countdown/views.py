from django.shortcuts import render

import datetime;

# Create your views here.

def index(request):
    now = datetime.datetime.now();
    timediff = 1

    brexit = datetime.datetime(2019, 3, 29, 23 - timediff)

    diff = brexit - now

    return render(request, 'countdown/index.html', {'timediff': timediff, 'date': diff, 'uk': brexit, 'now': now})

