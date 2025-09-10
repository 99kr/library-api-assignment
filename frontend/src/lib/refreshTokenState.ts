type RefreshTokenState = {
	maxAge: number
	expiresAt: number
}

export function getRefreshTokenState(): RefreshTokenState | null {
	return JSON.parse(localStorage.getItem('refresh_token_state') ?? 'null')
}

export function hasRefreshTokenState() {
	return !!localStorage.getItem('refresh_token_state')
}

export function setRefreshTokenState(state: RefreshTokenState) {
	localStorage.setItem('refresh_token_state', JSON.stringify(state))
}

export function removeRefreshTokenState() {
	localStorage.removeItem('refresh_token_state')
}
