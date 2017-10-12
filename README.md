# sdashboard
Reactive dashboard for software components built with Scala 2.12, Vert.x 3.4 and RxJava 2

## Want to change something and build locally?
All the current variables used in build are:
- secret (name of secret used in the keystore file, for the JWT generation - validation)

All the current variables used runtime are:
- DB_NAME (mongodb database name, will be used to store both your components and users)
- DB_URL (mongodb://username:password@host:port)

```
docker build \
    --build-arg secret=sdasbhoardSecret \
    --build-arg adminEmail=admin \
    --build-arg adminPass=admin \
    -t sdashbard:0.0.1 .
```

then run:
```
docker run -p 80:8080 \
    -e JWT_SECRET=sdasbhoard \
    -e DB_NAME=sdashboard \
    -e DB_URL=mongodb://username:password@host:port \
    -v $PWD/dashboard_example.json:/app/dashboard.json \
    sdashboard:0.0.1
```

## Dashboard bootstrap file
You can also have a json file that contains all the components for your dashboard, that will be executed when the api starts.
The file have to be in the root app path with the name `dashboard.json`.
The `dashboard_example.json` file is an example of how it should looks like.

*Important: all the components in the json file will not be registered in the database*