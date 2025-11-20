// AdminLayout.tsx
import React, { useState, useEffect } from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import {
  DashboardIcon,
  UsersIcon,
  MusicIcon,
  LogoutIcon,
  MenuIcon,
} from "../../components/Icons";

// Icon cho nút thu gọn/mở rộng sidebar
const ChevronLeftIcon: React.FC<React.SVGProps<SVGSVGElement>> = (props) => (
  <svg
    {...props}
    xmlns="http://www.w3.org/2000/svg"
    fill="none"
    viewBox="0 0 24 24"
    strokeWidth={1.5}
    stroke="currentColor"
  >
    <path
      strokeLinecap="round"
      strokeLinejoin="round"
      d="M15.75 19.5L8.25 12l7.5-7.5"
    />
  </svg>
);

const AdminLayout: React.FC = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  // State cho sidebar trên mobile (dạng overlay)
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  // State cho sidebar trên desktop (dạng thu gọn/mở rộng)
  const [isSidebarCollapsed, setIsSidebarCollapsed] = useState(false);

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  // Khóa cuộn trang khi sidebar mobile mở
  useEffect(() => {
    if (isSidebarOpen) {
      document.body.style.overflow = "hidden";
    } else {
      document.body.style.overflow = "";
    }
    // Cleanup function để đảm bảo overflow được reset khi component unmount
    return () => {
      document.body.style.overflow = "";
    };
  }, [isSidebarOpen]);

  const navItems = [
    { to: "/admin/dashboard", icon: <DashboardIcon />, label: "Dashboard" },
    { to: "/admin/users", icon: <UsersIcon />, label: "Quản lý Người dùng" },
    { to: "/admin/shows", icon: <MusicIcon />, label: "Quản lý Show diễn" },
  ];

  return (
    <div className="flex h-screen bg-[#16a34a]/5">
      {/* Lớp phủ mờ cho sidebar trên mobile */}
      {isSidebarOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-20 md:hidden"
          onClick={() => setIsSidebarOpen(false)}
          aria-hidden="true"
        ></div>
      )}

      {/* Sidebar */}
      <aside
        className={`
          bg-white border-r border-[#16a34a]/20 flex flex-col fixed inset-y-0 left-0 z-30
          w-64 transition-transform duration-300 ease-in-out
          md:relative md:translate-x-0
          ${isSidebarOpen ? "translate-x-0" : "-translate-x-full"}
          ${isSidebarCollapsed ? "md:w-20" : "md:w-64"}
        `}
      >
        {/* Nút bấm để thu gọn/mở rộng (chỉ hiển thị trên desktop) */}
        <button
          onClick={() => setIsSidebarCollapsed(!isSidebarCollapsed)}
          aria-label={
            isSidebarCollapsed ? "Mở rộng sidebar" : "Thu gọn sidebar"
          }
          className="hidden md:block absolute top-1/2 -right-3 -translate-y-1/2
                     bg-white border-2 border-[#16a34a]/30 rounded-full p-0.5
                     text-[#16a34a] hover:bg-[#16a34a]/10 transition-all z-40 shadow-sm"
        >
          <ChevronLeftIcon
            className={`w-4 h-4 transition-transform duration-300
              ${isSidebarCollapsed ? "rotate-180" : "rotate-0"}`}
          />
        </button>

        <div className="flex-1 flex flex-col overflow-hidden">
          {/* Logo/Header của Sidebar */}
          <div className="h-16 flex items-center justify-center text-2xl font-bold border-b border-[#16a34a]/20 text-[#16a34a] flex-shrink-0">
            {isSidebarCollapsed ? "S" : "ShowAdmin"}
          </div>

          {/* Navigation Links */}
          <nav className="flex-1 px-3 py-4">
            <ul>
              {navItems.map((item) => (
                <li key={item.to}>
                  <NavLink
                    to={item.to}
                    onClick={() => setIsSidebarOpen(false)} // Đóng sidebar khi click link trên mobile
                    className={({ isActive }) =>
                      `flex items-center px-3 py-2 mt-2 rounded-md transition-colors duration-200
                       ${isSidebarCollapsed ? "justify-center" : ""}
                       ${
                         isActive
                           ? "bg-[#16a34a]/10 text-[#16a34a] font-semibold"
                           : "text-[#1a3c34] hover:bg-[#16a34a]/5 hover:text-[#16a34a]"
                       }`
                    }
                  >
                    {React.cloneElement(item.icon, {
                      className: "w-5 h-5 flex-shrink-0",
                    })}
                    <span
                      className={`ml-3 transition-opacity duration-200 whitespace-nowrap
                        ${isSidebarCollapsed ? "md:hidden" : "md:inline"}`}
                    >
                      {item.label}
                    </span>
                  </NavLink>
                </li>
              ))}
            </ul>
          </nav>

          {/* Logout Button */}
          <div className="px-3 py-4 border-t border-[#16a34a]/20">
            <button
              onClick={handleLogout}
              className={`flex items-center w-full px-3 py-2 rounded-md transition-colors duration-200 text-[#1a3c34] hover:bg-[#16a34a]/5 hover:text-[#16a34a]
                    ${isSidebarCollapsed ? "justify-center" : ""}`}
            >
              <LogoutIcon className="w-5 h-5 flex-shrink-0" />
              <span
                className={`ml-3 transition-opacity duration-200
                  ${isSidebarCollapsed ? "md:hidden" : "md:inline"}`}
              >
                Đăng xuất
              </span>
            </button>
          </div>
        </div>
      </aside>

      {/* Main Content */}
      <div className="flex-1 flex flex-col overflow-hidden">
        <header className="h-16 bg-white border-b border-[#16a34a]/20 flex items-center justify-between px-6 flex-shrink-0">
          {/* Nút mở menu trên mobile */}
          <button
            className="text-[#16a34a] focus:outline-none md:hidden"
            onClick={() => setIsSidebarOpen(!isSidebarOpen)}
            aria-label="Mở menu"
          >
            <MenuIcon className="w-6 h-6" />
          </button>
          {/* Placeholder để đẩy user info sang phải trên desktop */}
          <div className="hidden md:block"></div>
          {/* User Info */}
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

export default AdminLayout;
