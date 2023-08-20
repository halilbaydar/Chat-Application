export const isNull = (str: any): boolean => {
    return str == null;
}

export const isEmpty = (str: string): boolean => {
    return str?.trim().length === 0
}

export const isBlank = (str: string): boolean => {
    return isNull(str) || isEmpty(str)
}