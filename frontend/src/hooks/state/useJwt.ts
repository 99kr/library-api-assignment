import { create } from 'zustand'

export type Role = 'ADMIN' | 'USER'

type JwtAccessTokenPayload = {
	sub: string
	type: string
	iat: number
	exp: number
	lastName: string
	firstName: string
	roles: Role[]
}

type Identity = {
	firstName: string
	lastName: string
	email: string
	roles: Role[]
	isLoggedIn: boolean
}

type JwtStore = {
	accessToken: string | null
	setAccessToken: (jwtToken: JwtStore['accessToken']) => void

	identity: Identity | null // null when fetching
	setIdentityFromJwtToken: (jwtToken: string) => void
	setIdentityAsLoggedOut: () => void

	hasRole: (role: Role) => boolean
	getMostPrivilegedRole: () => Role
}

export const useJwt = create<JwtStore>((set, get) => ({
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
				roles: payload.roles,
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
				roles: [],
				isLoggedIn: false,
			},
		}),

	hasRole: (role) => get().identity?.roles.includes(role) ?? false,

	getMostPrivilegedRole: () => {
		const roles = get().identity?.roles ?? []
		if (roles.includes('ADMIN')) return 'ADMIN'
		return 'USER'
	},
}))

export function parsePayloadFromJwt(jwtToken: string): JwtAccessTokenPayload {
	return JSON.parse(atob(jwtToken.split('.')[1]))
}
