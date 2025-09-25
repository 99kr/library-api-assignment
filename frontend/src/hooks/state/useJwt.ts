import { create } from 'zustand'

export type Role = 'ADMIN' | 'USER'

type JwtAccessTokenPayload = {
	sub: string
	type: string
	iat: number
	exp: number
	id: number
	lastName: string
	firstName: string
	roles: Role[]
}

type Identity = {
	id: number
	firstName: string
	lastName: string
	email: string
	roles: Role[]
	isLoggedIn: boolean
}

type JwtStore = {
	accessToken: string | null
	setAccessToken: (jwtToken: JwtStore['accessToken']) => void

	identity: Identity | null
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
				id: payload.id,
				firstName: payload.firstName,
				lastName: payload.lastName,
				email: payload.sub,
				roles: payload.roles,
				isLoggedIn: true,
			},
		})
	},
	setIdentityAsLoggedOut: () => set({ identity: null }),

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
