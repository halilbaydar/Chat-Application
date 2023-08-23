export default class log {
    public static info(str: string): void {
        console.log(str)
    }

    public static debug(str: string): void {
        console.debug(str)
    }

    public static error(str: string, error: any): void {
        console.error(str, error)
    }
}