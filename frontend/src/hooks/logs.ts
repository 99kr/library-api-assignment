import { useQuery } from '@/hooks/swr'

type LogsResponse = {
	id: number
	email: string
	action: string
	resource: string
	details: string | null
	timestamp: string
}[]

export const useLogs = () => useQuery<LogsResponse>('/logs')
