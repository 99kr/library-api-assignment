export class UnauthenticatedError extends Error {
	status = 401
	constructor() {
		super('Unauthenticated')
	}
}
