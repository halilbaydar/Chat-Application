export const hasCookieAccess = () => {
    if (typeof window === 'undefined' || !navigator.cookieEnabled) {
        return false;
    }
    try {
        return window.localStorage;
    } catch {
        return false;
    }
};