import { AccessDeniedError } from '@/errors/AccessDeniedError'
import { NotFoundError } from '@/errors/NotFoundError'
import { UnauthenticatedError } from '@/errors/UnauthenticatedError'
import { useJwt } from '@/hooks/state/useJwt'

const API_URL = 'http://localhost:8765/api/v1'

export type BaseResponse<T = unknown> = {
	errors: { field: string; message: string }[]
	data: T
}

export async function getRequest(url: string, options: RequestInit = {}) {
	const accessToken = useJwt.getState().accessToken

	options.headers = {
		'Content-Type': 'application/json',
	}

	if (accessToken) {
		options.headers.Authorization = `Bearer ${accessToken}`
	}

	const response = await fetch(API_URL + url, options)

	throwAppropiateError(response.status)

	return response.json()
}

export async function postRequest(url: string, body?: { arg: unknown }, options: RequestInit = {}) {
	const accessToken = useJwt.getState().accessToken

	options.headers = {
		'Content-Type': 'application/json',
	}

	if (accessToken) {
		options.headers.Authorization = `Bearer ${accessToken}`
	}

	options.method = 'POST'
	if (body) options.body = JSON.stringify(body.arg)

	const response = await fetch(API_URL + url, options)

	throwAppropiateError(response.status)

	return response.json()
}

export function postRequestWithCredentials(
	url: string,
	body?: { arg: unknown },
	options: RequestInit = {},
) {
	return postRequest(url, body, { ...options, credentials: 'include' })
}

function throwAppropiateError(status: number) {
	switch (status) {
		case 401:
			throw new UnauthenticatedError()
		case 403:
			throw new AccessDeniedError()
		case 404:
			throw new NotFoundError()
		default:
			break
	}
}

export async function getAccessTokenFromRefreshToken() {
	const refreshResponse = await fetch(`${API_URL}/auth/refresh`, {
		method: 'POST',
		credentials: 'include',
	})

	if (!refreshResponse.ok) return null

	const json = await refreshResponse.json()

	if (json.errors.length > 0) return null

	localStorage.setItem('has_refresh_token', 'true')

	return json.data.accessToken
}
