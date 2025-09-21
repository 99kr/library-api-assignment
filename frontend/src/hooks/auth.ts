import { useMutation, useMutationWithCredentials } from '@/hooks/swr'

type LoginResponse = { accessToken: string; refreshTokenDurationMs: number }
type LoginRequest = { email: string; password: string }

type LogoutResponse = { success: boolean }

type RegisterResponse = { success: boolean }
type RegisterRequest = { firstName: string; lastName: string; email: string; password: string }

export const useLogin = () => useMutationWithCredentials<LoginResponse, LoginRequest>('/auth/login')

export const useLogout = () => useMutationWithCredentials<LogoutResponse>('/auth/logout')

export const useRegister = () => useMutation<RegisterResponse, RegisterRequest>('/auth/register')
