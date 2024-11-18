# gl-finance-manager-be

## Lokális MYSQL adatbázis Docker
docker run --name gl-fm-mysql -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=dev-pw -e MYSQL_DATABASE=gl_finance_manager mysql

## Fejlesztői profil neve
dev

## Docker build
mvn clean package
docker build -t gl-finance-manager-be .

## Docker save
docker save -o gl-finance-manager-be.tar gl-finance-manager-be

## Docker load
docker load -i gl-finance-manager-be.tar

## Docker run
docker run -e -p8080:8080 -p9090:9090 gl-finance-manager-be