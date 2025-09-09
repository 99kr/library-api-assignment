export class AccessDeniedError extends Error {
	status = 403
	constructor() {
		super('Access denied')
	}
}
