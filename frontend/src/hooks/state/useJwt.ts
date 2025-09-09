import { create } from 'zustand'

type JwtAccessTokenPayload = {
	sub: string
	type: string
	iat: number
	exp: number
	lastName: string
	firstName: string
	authorities: { authority: string }[]
}

type Identity = {
	firstName: string
	lastName: string
	email: string
	isLoggedIn: boolean
}

type JwtStore = {
	accessToken: string | null
	setAccessToken: (jwtToken: JwtStore['accessToken']) => void

	identity: Identity | null // null when fetching
	setIdentityFromJwtToken: (jwtToken: string) => void
	setIdentityAsLoggedOut: () => void
}

export const useJwt = create<JwtStore>((set) => ({
	accessToken: null,
	setAccessToken: (accessToken) => set({ accessToken }),

	identity: null,
	setIdentityFromJwtToken: (jwtToken) => {
		const payload = parsePayloadFromJwt(jwtToken)

		set({
			identity: {
				firstName: payload.firstName,
				lastName: payload.lastName,
				email: payload.sub,
				isLoggedIn: true,
			},
		})
	},

	setIdentityAsLoggedOut: () =>
		set({
			identity: {
				firstName: '',
				lastName: '',
				email: '',
				isLoggedIn: false,
			},
		}),
}))

export function parsePayloadFromJwt(jwtToken: string): JwtAccessTokenPayload {
	return JSON.parse(atob(jwtToken.split('.')[1]))
}
