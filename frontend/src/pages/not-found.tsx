import { Link } from 'react-router'
import { Button } from '@/components/ui/button'

export function NotFound() {
	return (
		<div className='flex flex-col items-center justify-center h-full gap-8 -mt-16'>
			<p className='text-xl text-muted-foreground'>Oops!</p>
			<h1 className='text-9xl font-black text-primary'>404</h1>
			<p className='text-2xl'>Page not found</p>
			<Button variant='outline' size='lg' asChild>
				<Link to='/'>Back to home</Link>
			</Button>
		</div>
	)
}
