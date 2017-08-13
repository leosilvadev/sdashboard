# sdashboard
Reactive dashboard for software components built with Scala 2.12, Vert.x 3.4 and RxJava 2

## Want to change something and build locally?
All the current used variables are:
- DB_NAME (mongodb database name, will be used to store both your components and users)
- DB_URL (mongodb://username:password@host:port)
- JWT_SECRET (name of secret used in the keystore file, for the JWT generation - validation)

```
docker build --build-arg JWT_SECRET=sdasbhoard -t sdashbard:0.0.1 .
```

then run:
```
docker run -p 80:8080 -e JWT_SECRET=sdasbhoard -e DB_NAME=sdashboard -e DB_URL=mongodb://username:password@host:port sdashboard:0.0.1
```

