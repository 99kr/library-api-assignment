import { Badge } from '@/components/ui/badge'
import {
	Table,
	TableBody,
	TableCell,
	TableHead,
	TableHeader,
	TableRow,
} from '@/components/ui/table'
import { type Loan, useUserLoans } from '@/hooks/loans'
import { useJwt } from '@/hooks/state/useJwt'

type BadgeProps = Parameters<typeof Badge>[0]

const loanStateToBadgeVariant: Record<Loan['state'], BadgeProps['variant']> = {
	BORROWED: 'green',
	EXPIRED: 'red',
	RETURNED: 'yellow',
	RETURNED_LATE: 'red',
}

export function MyLoans() {
	const { identity } = useJwt()
	const { data: loans } = useUserLoans(identity?.id ?? null)

	if (!loans) return null

	return (
		<>
			<h1 className='text-4xl font-semibold'>My loans</h1>
			<p>{loans.data.length} records found</p>

			<Table className='mt-4'>
				<TableHeader>
					<TableRow>
						<TableHead className='w-[100px]'>Loan ID</TableHead>
						<TableHead>Book</TableHead>
						<TableHead>Borrowed at</TableHead>
						<TableHead>Due at</TableHead>
						<TableHead>Returned at</TableHead>
						<TableHead className='text-right'>Status</TableHead>
					</TableRow>
				</TableHeader>
				<TableBody>
					{loans.data.map((loan) => (
						<TableRow key={loan.id}>
							<TableCell className='font-medium'>{loan.id}</TableCell>
							<TableCell>{loan.book.title}</TableCell>
							<TableCell>
								{new Date(loan.borrowedAt).toLocaleString('sv-SE')}
							</TableCell>
							<TableCell>{new Date(loan.dueAt).toLocaleString('sv-SE')}</TableCell>
							<TableCell>
								{loan.returnedAt &&
									new Date(loan.returnedAt).toLocaleString('sv-SE')}
							</TableCell>
							<TableCell className='text-right'>
								<Badge
									variant={loanStateToBadgeVariant[loan.state]}
									className='capitalize'
								>
									{loan.state.toLowerCase().replace('_', ' ')}
								</Badge>
							</TableCell>
						</TableRow>
					))}
				</TableBody>
			</Table>
		</>
	)
}
