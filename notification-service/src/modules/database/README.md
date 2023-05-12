### add migrations 
npx sequelize-cli migration:generate --name create-user

### run migrations 
npx sequelize-cli db:migrate

### add seed file 
npx sequelize-cli seed:generate --name add-users

### run seed files
npx sequelize db:seed:all 

