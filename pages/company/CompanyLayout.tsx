import React, { useState } from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import {
  DashboardIcon,
  ArtistIcon,
  LogoutIcon,
  MenuIcon,
} from "../../components/Icons";
import type { Company } from "../../types";

const CompanyLayout: React.FC = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const companyUser = user as Company;

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const navItems = [
    {
      to: "/company/dashboard",
      icon: <DashboardIcon />,
      label: "Show của tôi",
    },
    { to: "/company/artists", icon: <ArtistIcon />, label: "Quản lý ca sĩ" },
  ];

  return (
    <div className="flex h-screen bg-[#16a34a]/5">
      {/* Overlay for mobile */}
      {isSidebarOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-20 md:hidden"
          onClick={() => setIsSidebarOpen(false)}
        ></div>
      )}

      {/* Sidebar */}
      <aside
        className={`w-64 bg-white border-r border-[#16a34a]/20 flex flex-col fixed inset-y-0 left-0 z-30 transform ${
          isSidebarOpen ? "translate-x-0" : "-translate-x-full"
        } transition-transform duration-300 ease-in-out md:relative md:translate-x-0`}
      >
        <div className="h-16 flex items-center justify-center text-xl font-bold border-b border-[#16a34a]/20 text-center px-2 text-[#16a34a]">
          {companyUser?.companyName || "Company Portal"}
        </div>
        <nav className="flex-1 px-4 py-4">
          <ul>
            {navItems.map((item) => (
              <li key={item.to}>
                <NavLink
                  to={item.to}
                  onClick={() => setIsSidebarOpen(false)}
                  className={({ isActive }) =>
                    `flex items-center px-4 py-2 mt-2 rounded-md transition-colors duration-200 ${
                      isActive
                        ? "bg-[#16a34a]/10 text-[#16a34a] font-semibold"
                        : "text-[#1a3c34] hover:bg-[#16a34a]/5 hover:text-[#16a34a]"
                    }`
                  }
                >
                  {React.cloneElement(item.icon, { className: "w-5 h-5" })}
                  <span className="ml-3">{item.label}</span>
                </NavLink>
              </li>
            ))}
          </ul>
        </nav>
        <div className="px-4 py-4 border-t border-[#16a34a]/20">
          <button
            onClick={handleLogout}
            className="flex items-center w-full px-4 py-2 mt-2 text-[#1a3c34] rounded-md hover:bg-[#16a34a]/5 hover:text-[#16a34a] transition-colors duration-200"
          >
            <LogoutIcon className="w-5 h-5" />
            <span className="ml-3">Đăng xuất</span>
          </button>
        </div>
      </aside>

      {/* Main content */}
      <div className="flex-1 flex flex-col overflow-hidden">
        <header className="h-16 bg-white border-b border-[#16a34a]/20 flex items-center justify-between md:justify-end px-6">
          <button
            className="text-[#16a34a] focus:outline-none md:hidden"
            onClick={() => setIsSidebarOpen(!isSidebarOpen)}
          >
            <MenuIcon className="w-6 h-6" />
          </button>
          <div className="flex items-center">
            <span className="text-[#1a3c34] font-semibold">
              Chào, {user?.name}
            </span>
          </div>
        </header>
        <main className="flex-1 overflow-x-hidden overflow-y-auto bg-[#16a34a]/5 p-4 sm:p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default CompanyLayout;
