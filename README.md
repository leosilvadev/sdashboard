# sdashboard
Reactive dashboard for software components built with Scala 2.12, Vert.x 3.4 and RxJava 2





## Want to change something and build locally?
```
docker build -t sdashbard:0.0.1 .
```

then run:
```
docker run -p 80:8080 -e DB_NAME=sdashboard -e DB_URL=mongodb://username:password@host:port/sdashboard sdashboard:0.0.1
```

