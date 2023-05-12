import * as dotenv from 'dotenv';

dotenv.config();

export default {
    development: {
        default: {
            username: process.env.DB_USER,
            password: process.env.DB_PASS,
            database: process.env.DB_NAME_GLOBAL,
            host: process.env.DB_HOST,
            port: process.env.DB_PORT,
            dialect: process.env.DB_DIALECT,
        },
    },
    test: {
        default: {
            username: process.env.DB_USER,
            password: process.env.DB_PASS,
            database: process.env.DB_NAME_GLOBAL,
            host: process.env.DB_HOST,
            port: process.env.DB_PORT,
            dialect: process.env.DB_DIALECT,
        },

    },
    staging: {
        default: {
            username: process.env.DB_USER,
            password: process.env.DB_PASS,
            database: process.env.DB_NAME_GLOBAL,
            host: process.env.DB_HOST,
            port: process.env.DB_PORT,
            dialect: process.env.DB_DIALECT,
        },

    },
    production: {
        default: {
            username: process.env.DB_USER,
            password: process.env.DB_PASS,
            database: process.env.DB_NAME_GLOBAL,
            host: process.env.DB_HOST,
            port: process.env.DB_PORT,
            dialect: process.env.DB_DIALECT,
        },
    },
};
