import {
	Table,
	TableBody,
	TableCell,
	TableHead,
	TableHeader,
	TableRow,
} from '@/components/ui/table'
import { useLogsQuery } from '@/hooks/api/useLogsQuery'

export function Logs() {
	const { data: logs } = useLogsQuery()

	if (!logs) return null

	return (
		<>
			<h1 className='text-4xl font-semibold'>Audit logs</h1>
			<p>{logs.data.length} records found</p>

			<Table className='mt-4'>
				<TableHeader>
					<TableRow>
						<TableHead className='w-[100px]'>Log ID</TableHead>
						<TableHead>Email</TableHead>
						<TableHead>Action</TableHead>
						<TableHead>Resource</TableHead>
						<TableHead>Details</TableHead>
						<TableHead className='text-right'>Timestamp</TableHead>
					</TableRow>
				</TableHeader>
				<TableBody>
					{logs.data.map((log) => (
						<TableRow key={log.id}>
							<TableCell className='font-medium'>{log.id}</TableCell>
							<TableCell>{log.email}</TableCell>
							<TableCell>{log.action}</TableCell>
							<TableCell>{log.resource}</TableCell>
							<TableCell>{log.details}</TableCell>
							<TableCell className='text-right'>
								{new Date(log.timestamp).toLocaleString('sv-SE')}
							</TableCell>
						</TableRow>
					))}
				</TableBody>
			</Table>
		</>
	)
}
