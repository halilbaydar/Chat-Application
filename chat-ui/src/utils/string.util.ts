export const isNull = (str: any): boolean => {
    return str == null;
}

export const isEmpty = (str: string): boolean => {
    return isNull(str) || str?.length === 0
}

export const isNotEmpty = (str: string): boolean => {
    return !isEmpty(str)
}

export const isBlank = (str: string): boolean => {
    return isEmpty(str) || str?.trim().length === 0
}