import useSWRMutation from 'swr/mutation'
import { type BaseResponse, postRequestWithCredentials } from '@/lib/api'

export type LoginResponse = BaseResponse<{ accessToken: string; refreshTokenDurationMs: number }>
type LoginRequest = { email: string; password: string }

export function useLoginMutation() {
	return useSWRMutation<LoginResponse, unknown, string, LoginRequest>(
		'/auth/login',
		postRequestWithCredentials,
	)
}
