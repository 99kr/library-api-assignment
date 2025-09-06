const API_URL = 'http://localhost:8765/api/v1'

export type BaseResponse<T = unknown> = {
	errors: { field: string; message: string }[]
	data: T
}

export async function getRequest(url: string, options: RequestInit = {}) {
	options.credentials = 'include'
	options.headers ??= {
		'Content-Type': 'application/json',
	}

	const response = await fetch(API_URL + url, options)

	// TODO: Shouldn't throw an error, will fix when i have a global exception handler in the api
	if (!response.ok) {
		if (response.headers.get('Content-Type')?.includes('application/json')) {
			const res = (await response.json()) as BaseResponse
			throw new Error(res.errors[0].message)
		} else {
			throw new Error('Something went wrong')
		}
	}

	return response.json()
}

export async function postRequest(url: string, body: { arg: unknown }, options: RequestInit = {}) {
	options.credentials = 'include'
	options.headers ??= {
		'Content-Type': 'application/json',
	}
	options.method = 'POST'
	options.body = JSON.stringify(body.arg)

	const response = await fetch(API_URL + url, options)

	if (!response.ok) {
		if (response.headers.get('Content-Type')?.includes('application/json')) {
			const res = (await response.json()) as BaseResponse
			throw new Error(res.errors[0].message)
		} else {
			throw new Error('Something went wrong')
		}
	}

	return response.json()
}
