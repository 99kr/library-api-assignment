import { LogOut, User2 } from 'lucide-react'
import type { PropsWithChildren } from 'react'
import { Link } from 'react-router'
import {
	DropdownMenu,
	DropdownMenuContent,
	DropdownMenuItem,
	DropdownMenuSeparator,
	DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'

export function ProfileDropdown({ children }: PropsWithChildren) {
	return (
		<DropdownMenu>
			<DropdownMenuTrigger asChild>{children}</DropdownMenuTrigger>
			<DropdownMenuContent className='w-56'>
				<DropdownMenuItem asChild>
					<Link to='/not-implemented' className='hover:cursor-pointer'>
						<User2 className='size-4' />
						<span>My profile</span>
					</Link>
				</DropdownMenuItem>
				<DropdownMenuSeparator />
				<DropdownMenuItem variant='destructive' asChild>
					<Link to='/logout' className='hover:cursor-pointer'>
						<LogOut className='size-4' />
						<span>Log out</span>
					</Link>
				</DropdownMenuItem>
			</DropdownMenuContent>
		</DropdownMenu>
	)
}
