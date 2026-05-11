import { SidebarProvider, SidebarInset, SidebarTrigger } from '../ui/sidebar';
import { AppSidebar } from './AppSidebar';
import { Outlet, useLocation } from 'react-router-dom';

interface LayoutProps {
  onAdd: () => void;
}

export const Layout = ({ onAdd }: LayoutProps) => {
  const location = useLocation();

  const pageTitles: Record<string, string> = {
    '/dashboard': 'Dashboard',
    '/analytics': 'Analytics',
  };

  const currentPage = pageTitles[location.pathname];
  return (
    <SidebarProvider className="bg-muted/40">
      <AppSidebar onAdd={onAdd} />
      <SidebarInset className="bg-background">
        <header className="flex h-16 shrink-0 items-center gap-2 px-4 md:px-8 border-b border-border">
          <SidebarTrigger className="-ml-1" />
          <div className="h-4 w-px bg-border mx-2" />
          <h2 className="text-sm font-semibold uppercase tracking-wider text-muted-foreground">
            {currentPage}
          </h2>
        </header>
        <div className="flex flex-1 flex-col gap-4 p-4 md:gap-8 md:p-8">
          <Outlet />
        </div>
      </SidebarInset>
    </SidebarProvider>
  );
};
