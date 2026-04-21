import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuItem,
  SidebarMenuButton,
  SidebarGroupContent
} from "@/components/ui/sidebar";
import { LayoutDashboard, CirclePlus, Briefcase } from "lucide-react";

const nav = [
  {
    title: 'Dashboard',
    url: '#dash',
    icon: LayoutDashboard,
  }
];

export function AppSidebar({ ...props }: React.ComponentProps<typeof Sidebar>) {
  return (
    <Sidebar collapsible='icon' {...props}>
      <SidebarHeader>
        <SidebarMenu>
          <SidebarMenuItem>
            <SidebarMenuButton asChild className='data-[slot=sidebar-menu-button]:p-1.5!'>
              <a href='#home'>
                <Briefcase className='size-5!' />
                <span className='text-base font-semibold'>Job Tracker</span>
              </a>
            </SidebarMenuButton>
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarHeader>
      <SidebarContent>
        <SidebarGroup className='flex flex-col gap-2'>
          <SidebarGroupContent>
            <SidebarMenu>
              <SidebarMenuItem className='flex items-center gap-2'>
                <SidebarMenuButton className='min-w-8 bg-primary text-primary-foreground duration-200 ease-linear hover:bg-primary/90 hover:text-primary-foreground active:bg-primary/90 active:text-primary-foreground'>
                  <CirclePlus></CirclePlus>
                  <span>Add Job</span>
                </SidebarMenuButton>
              </SidebarMenuItem>
            </SidebarMenu>
            <SidebarMenu className='mt-2'>
              {nav.map((navItem) => (
                <SidebarMenuItem key={navItem.title}>
                  <SidebarMenuButton asChild>
                    <a href={navItem.url}>
                      {navItem.icon && <navItem.icon />}
                      <span>{navItem.title}</span>
                    </a>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
    </Sidebar>
  )
}