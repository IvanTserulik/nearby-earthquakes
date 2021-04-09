# nearby-earthquakes
[USGS Earthquake Hazards Program](https://earthquake.usgs.gov/aboutus/) is an organization that analyze earthquake threats around the world. They expose REST API outlining details of recent earthquakes happening around the world - location, magnitude, etc.  
  
Thr program will calculate for a given city (lat/lon) 10 most nearby earthquakes (earthquakes that happened in the closest proximity of that city).

## Getting list of earthquakes
Web services for fetching Earthquakes are located here: https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php  
Service takes earthquakes that happened during last 30 days: https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson  

## Calculating distance
The program calculates curve distance between two lat/lon points.

## Program input
Program accepts two numbers on standard input: the latitude and longitude of a city. So for New York the program should be started with numbers:  
```
40.730610  
-73.935242  
```
Source: https://www.latlong.net/place/new-york-city-ny-usa-1848.html

## Program output 
As an output, the program lists **10** earthquakes that happened in the closest proximity to input point, in the order from the closest to the furthest. For each earthquake program prints the content of a `title` field followed by ` || ` and `distance` (rounded to full kilometers).  
```
title || distance  
title || distance  
title || distance  
```