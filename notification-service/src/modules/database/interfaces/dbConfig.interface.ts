export interface IDatabaseConfigAttributes {
    username?: string;
    password?: string;
    database?: string;
    host?: string;
    port?: number | string;
    dialect?: string;
    urlDatabase?: string;
}

export interface IDatabaseConfig {
    development: {
        default: IDatabaseConfigAttributes;
    };
    test: {
        default: IDatabaseConfigAttributes;
    };
    production: {
        default: IDatabaseConfigAttributes;
    };
}
