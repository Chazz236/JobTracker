import { AppSidebar } from "./components/AppSidebar";
import Dashboard from "./pages/Dashboard";
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";

function App() {
  return (
    <SidebarProvider>
      <AppSidebar />
      <main>
        <header>
          <SidebarTrigger />
        </header>

        <div>
          <Dashboard />
        </div>
      </main>
    </SidebarProvider>
  );
}

export default App;
