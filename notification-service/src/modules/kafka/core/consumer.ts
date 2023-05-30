export interface Consumer<T, C> {
    consume(payload: T, context: C): Promise<void>
}