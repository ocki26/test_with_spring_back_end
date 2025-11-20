import React, {
  useState,
  useMemo,
  useEffect,
  memo,
  useCallback,
  useRef,
} from "react";
import { useData } from "../../contexts/DataContext";
import Chart from "react-apexcharts";
import { ApexOptions } from "apexcharts";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import viLocale from "@fullcalendar/core/locales/vi";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { vi } from "date-fns/locale";
import { SortUpIcon, SortDownIcon, CloseIcon } from "../../components/Icons";

// 🔹 DEBUG: Custom Hook để theo dõi re-render
function useWhyDidYouUpdate(name: string, props: any) {
  const previousProps = useRef<any>();

  useEffect(() => {
    if (previousProps.current) {
      const allKeys = Object.keys({ ...previousProps.current, ...props });
      const changes: any = {};

      allKeys.forEach((key) => {
        if (previousProps.current[key] !== props[key]) {
          changes[key] = {
            from: previousProps.current[key],
            to: props[key],
          };
        }
      });

      if (Object.keys(changes).length > 0) {
        // console.log(`🔄 [why-did-you-update] ${name}`, changes);
      }
    }

    previousProps.current = props;
  });
}

// 🔹 Custom Hook để "debounce" giá trị
function useDebounce<T>(value: T, delay: number): T {
  const [debouncedValue, setDebouncedValue] = useState<T>(value);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedValue(value);
    }, delay);

    return () => {
      clearTimeout(handler);
    };
  }, [value, delay]);

  return debouncedValue;
}

// 🔹 MODAL CHI TIẾT CÔNG TY (CHỈ HIỂN THỊ THÔNG TIN)
const CompanyDetailModal: React.FC<{
  isOpen: boolean;
  onClose: () => void;
  company: any;
}> = ({ isOpen, onClose, company }) => {
  if (!isOpen || !company) return null;

  const formatCurrency = (value: number) =>
    new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(value);

  return (
    <div className="fixed inset-0 bg-black bg-opacity-60 z-50 flex justify-center items-center p-4 transition-opacity">
      <div className="bg-white rounded-xl shadow-2xl w-full max-w-md max-h-[90vh] flex flex-col animate-scale-in">
        {/* Header */}
        <div className="flex justify-between items-center p-6 border-b border-[#16a34a]/20 bg-[#16a34a]/10 rounded-t-xl">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 bg-[#16a34a] rounded-lg flex items-center justify-center">
              <span className="text-white font-bold text-lg">
                {company.name?.charAt(0) || "C"}
              </span>
            </div>
            <div>
              <h2 className="text-xl font-bold text-[#1a3c34]">
                {company.name}
              </h2>
              <p className="text-[#16a34a] text-sm">Thông tin công ty</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="text-[#16a34a] hover:text-[#15803d] p-2 hover:bg-[#16a34a]/10 rounded-lg transition-colors"
          >
            <CloseIcon />
          </button>
        </div>

        {/* Content */}
        <div className="overflow-y-auto p-6 space-y-4">
          <div className="space-y-3">
            <div className="flex justify-between items-center p-3 bg-[#16a34a]/10 rounded-lg">
              <span className="text-[#16a34a] font-medium">Tên công ty:</span>
              <span className="text-[#1a3c34] font-semibold">
                {company.name}
              </span>
            </div>

            <div className="flex justify-between items-center p-3 bg-[#16a34a]/10 rounded-lg">
              <span className="text-[#16a34a] font-medium">Email:</span>
              <span className="text-[#1a3c34] font-semibold">
                {company.email}
              </span>
            </div>

            <div className="flex justify-between items-center p-3 bg-[#16a34a]/10 rounded-lg">
              <span className="text-[#16a34a] font-medium">Doanh thu:</span>
              <span className="text-[#1a3c34] font-semibold">
                {formatCurrency(company.revenue || 0)}
              </span>
            </div>

            <div className="flex justify-between items-center p-3 bg-[#16a34a]/10 rounded-lg">
              <span className="text-[#16a34a] font-medium">Tổng số show:</span>
              <span className="text-[#1a3c34] font-semibold">
                {company.totalShows || 0} show
              </span>
            </div>

            <div className="flex justify-between items-center p-3 bg-[#16a34a]/10 rounded-lg">
              <span className="text-[#16a34a] font-medium">ID:</span>
              <span className="text-[#1a3c34] font-mono text-sm">
                {company.id}
              </span>
            </div>
          </div>
        </div>

        {/* Footer chỉ có nút đóng */}
        <div className="flex justify-end p-4 border-t border-[#16a34a]/20 bg-[#16a34a]/10 rounded-b-xl">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-[#16a34a] text-white rounded-lg hover:bg-[#15803d] transition-colors font-medium"
          >
            Đóng
          </button>
        </div>
      </div>
    </div>
  );
};

// 🔹 MODAL CHI TIẾT SHOW (CHỈ HIỂN THỊ THÔNG TIN)
const ShowDetailModal: React.FC<{
  isOpen: boolean;
  onClose: () => void;
  show: any;
}> = ({ isOpen, onClose, show }) => {
  if (!isOpen || !show) return null;

  const formatCurrency = (value: number) =>
    new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(value);

  const formatDate = (date: Date) =>
    new Intl.DateTimeFormat("vi-VN", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    }).format(date);

  return (
    <div className="fixed inset-0 bg-black bg-opacity-60 z-50 flex justify-center items-center p-4 transition-opacity">
      <div className="bg-white rounded-xl shadow-2xl w-full max-w-md max-h-[90vh] flex flex-col animate-scale-in">
        {/* Header */}
        <div className="flex justify-between items-center p-6 border-b border-[#16a34a]/20 bg-[#16a34a]/10 rounded-t-xl">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 bg-[#16a34a] rounded-lg flex items-center justify-center">
              <span className="text-white font-bold text-lg">
                {show.name?.charAt(0) || "S"}
              </span>
            </div>
            <div>
              <h2 className="text-xl font-bold text-[#1a3c34]">{show.name}</h2>
              <p className="text-[#16a34a] text-sm">Thông tin show</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="text-[#16a34a] hover:text-[#15803d] p-2 hover:bg-[#16a34a]/10 rounded-lg transition-colors"
          >
            <CloseIcon />
          </button>
        </div>

        {/* Content */}
        <div className="overflow-y-auto p-6 space-y-4">
          <div className="space-y-3">
            <div className="flex justify-between items-center p-3 bg-[#16a34a]/10 rounded-lg">
              <span className="text-[#16a34a] font-medium">Tên show:</span>
              <span className="text-[#1a3c34] font-semibold">{show.name}</span>
            </div>

            <div className="flex justify-between items-center p-3 bg-[#16a34a]/10 rounded-lg">
              <span className="text-[#16a34a] font-medium">Địa điểm:</span>
              <span className="text-[#1a3c34] font-semibold">{show.venue}</span>
            </div>

            <div className="flex justify-between items-center p-3 bg-[#16a34a]/10 rounded-lg">
              <span className="text-[#16a34a] font-medium">Thời gian:</span>
              <span className="text-[#1a3c34] font-semibold">
                {formatDate(show.date)}
              </span>
            </div>

            <div className="flex justify-between items-center p-3 bg-[#16a34a]/10 rounded-lg">
              <span className="text-[#16a34a] font-medium">Doanh thu:</span>
              <span className="text-[#1a3c34] font-semibold">
                {formatCurrency(show.revenue || 0)}
              </span>
            </div>

            <div className="flex justify-between items-center p-3 bg-[#16a34a]/10 rounded-lg">
              <span className="text-[#16a34a] font-medium">Vé đã bán:</span>
              <span className="text-[#1a3c34] font-semibold">
                {show.soldTickets?.toLocaleString("vi-VN")} /{" "}
                {show.totalTickets?.toLocaleString("vi-VN")}
              </span>
            </div>

            <div className="flex justify-between items-center p-3 bg-[#16a34a]/10 rounded-lg">
              <span className="text-[#16a34a] font-medium">Tỷ lệ bán:</span>
              <span className="text-[#1a3c34] font-semibold">
                {show.sellThrough?.toFixed(1)}%
              </span>
            </div>

            <div className="flex justify-between items-center p-3 bg-[#16a34a]/10 rounded-lg">
              <span className="text-[#16a34a] font-medium">Trạng thái:</span>
              <span
                className={`px-2 py-1 rounded-full text-xs font-medium ${
                  show.status === "Sắp diễn"
                    ? "bg-[#16a34a]/20 text-[#1a3c34]"
                    : "bg-gray-100 text-gray-800"
                }`}
              >
                {show.status}
              </span>
            </div>

            {show.artists && show.artists.length > 0 && (
              <div className="p-3 bg-[#16a34a]/10 rounded-lg">
                <span className="text-[#16a34a] font-medium block mb-2">
                  Nghệ sĩ:
                </span>
                <div className="flex flex-wrap gap-1">
                  {show.artists.map((artist: string, index: number) => (
                    <span
                      key={index}
                      className="bg-[#16a34a]/20 text-[#1a3c34] px-2 py-1 rounded-full text-xs"
                    >
                      {artist}
                    </span>
                  ))}
                </div>
              </div>
            )}
          </div>
        </div>

        {/* Footer chỉ có nút đóng */}
        <div className="flex justify-end p-4 border-t border-[#16a34a]/20 bg-[#16a34a]/10 rounded-b-xl">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-[#16a34a] text-white rounded-lg hover:bg-[#15803d] transition-colors font-medium"
          >
            Đóng
          </button>
        </div>
      </div>
    </div>
  );
};

// 🔹 COMPONENT PHÂN TRANG (Tối ưu responsive)
const Pagination = memo(
  ({
    currentPage,
    totalPages,
    onPageChange,
    itemsPerPage,
    onItemsPerPageChange,
    totalItems,
  }: {
    currentPage: number;
    totalPages: number;
    onPageChange: (page: number) => void;
    itemsPerPage: number;
    onItemsPerPageChange: (value: number) => void;
    totalItems: number;
  }) => {
    if (totalItems === 0) {
      return null;
    }

    const handlePrevious = useCallback(() => {
      if (currentPage > 1) {
        onPageChange(currentPage - 1);
      }
    }, [currentPage, onPageChange]);

    const handleNext = useCallback(() => {
      if (currentPage < totalPages) {
        onPageChange(currentPage + 1);
      }
    }, [currentPage, totalPages, onPageChange]);

    const handleItemsPerPageChange = useCallback(
      (e: React.ChangeEvent<HTMLSelectElement>) => {
        const newValue = Number(e.target.value);
        onItemsPerPageChange(newValue);
      },
      [onItemsPerPageChange]
    );

    return (
      <div className="flex flex-col md:flex-row justify-between items-center mt-6 p-4 bg-[#16a34a]/10 rounded-lg border border-[#16a34a]/20 gap-4">
        <div className="w-full md:w-auto flex justify-between md:justify-start items-center text-sm text-[#16a34a]">
          <div className="flex items-center gap-2">
            <span>Hiển thị</span>
            <select
              value={itemsPerPage}
              onChange={handleItemsPerPageChange}
              className="border border-[#16a34a]/30 rounded-md px-2 py-1.5 bg-white focus:outline-none focus:ring-2 focus:ring-[#16a34a]"
            >
              <option value={12}>12</option>
              <option value={24}>24</option>
              <option value={48}>48</option>
              <option value={96}>96</option>
            </select>
          </div>
          <span className="text-[#16a34a] md:ml-2">
            / {totalItems.toLocaleString("vi-VN")} mục
          </span>
        </div>

        <div className="w-full md:w-auto flex items-center justify-between md:justify-end gap-2">
          <button
            onClick={handlePrevious}
            disabled={currentPage === 1}
            className="flex-1 md:flex-none px-4 py-2 border border-[#16a34a]/30 rounded-md text-sm font-medium disabled:opacity-50 disabled:cursor-not-allowed hover:bg-[#16a34a]/10 bg-white transition-colors text-[#16a34a]"
          >
            ← Trước
          </button>

          <span className="px-3 py-1 bg-white border border-[#16a34a]/30 rounded-md text-sm font-semibold text-[#16a34a] min-w-[80px] text-center">
            {currentPage} / {totalPages}
          </span>

          <button
            onClick={handleNext}
            disabled={currentPage === totalPages}
            className="flex-1 md:flex-none px-4 py-2 border border-[#16a34a]/30 rounded-md text-sm font-medium disabled:opacity-50 disabled:cursor-not-allowed hover:bg-[#16a34a]/10 bg-white transition-colors text-[#16a34a]"
          >
            Sau →
          </button>
        </div>
      </div>
    );
  }
);
Pagination.displayName = "Pagination";

// 🔹 MEMOIZED CHART COMPONENT
const RevenueTrendChart = memo(
  ({ filteredShows }: { filteredShows: any[] }) => {
    const chartData = useMemo(() => {
      const revenueByMonth: { [key: string]: number } = {};
      filteredShows.forEach((show) => {
        if (show.datetime && typeof show.datetime === "string") {
          const month = show.datetime.substring(0, 7);
          revenueByMonth[month] =
            (revenueByMonth[month] || 0) + (show.revenue || 0);
        }
      });
      const revenueTrendData = Object.keys(revenueByMonth)
        .sort()
        .map((month) => ({ month, revenue: revenueByMonth[month] }));
      return {
        series: [
          { name: "Doanh thu", data: revenueTrendData.map((d) => d.revenue) },
        ],
        categories: revenueTrendData.map((d) => d.month),
      };
    }, [filteredShows]);

    const formatCurrency = (value: number) => {
      return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
      }).format(value);
    };

    const revenueTrendOptions: ApexOptions = {
      colors: ["#16a34a"],
      chart: {
        fontFamily: "inherit",
        toolbar: { show: false },
        animations: { enabled: true },
        zoom: { enabled: false },
      },
      xaxis: {
        categories: chartData.categories,
        labels: {
          rotate: -45,
          style: { colors: "#6B7280", fontSize: "11px" },
          formatter: (value) => {
            if (!value) return "";
            const [year, month] = value.split("-");
            return `T${month}/${year}`;
          },
        },
      },
      yaxis: {
        labels: {
          formatter: (val) =>
            val >= 1000000000
              ? `${(val / 1000000000).toFixed(1)}B`
              : val >= 1000000
              ? `${(val / 1000000).toFixed(1)}M`
              : `${(val / 1000).toFixed(0)}K`,
          style: { colors: "#6B7280", fontSize: "11px" },
        },
      },
      tooltip: {
        theme: "light",
        y: {
          formatter: (val) => formatCurrency(val),
          title: { formatter: () => "Doanh thu" },
        },
      },
      stroke: { curve: "smooth", width: 2, colors: ["#16a34a"] },
      fill: {
        type: "gradient",
        gradient: {
          shadeIntensity: 1,
          opacityFrom: 0.7,
          opacityTo: 0.1,
          stops: [0, 90, 100],
          colorStops: [
            {
              offset: 0,
              color: "#16a34a",
              opacity: 0.7,
            },
            {
              offset: 100,
              color: "#16a34a",
              opacity: 0.1,
            },
          ],
        },
      },
      dataLabels: { enabled: false },
      grid: { borderColor: "#f1f5f9", strokeDashArray: 5 },
    };

    if (!chartData || chartData.categories.length === 0) {
      return (
        <div className="bg-white p-4 sm:p-6 rounded-lg shadow border border-[#16a34a]/20">
          <h2 className="text-lg sm:text-xl font-semibold text-[#1a3c34] mb-4">
            Xu Hướng Doanh Thu Theo Tháng
          </h2>
          <div className="h-64 flex items-center justify-center text-[#16a34a] bg-[#16a34a]/10 rounded-lg text-sm">
            <p>Không có dữ liệu để hiển thị biểu đồ.</p>
          </div>
        </div>
      );
    }

    return (
      <div className="bg-white p-4 sm:p-6 rounded-lg shadow border border-[#16a34a]/20 w-full overflow-hidden">
        <h2 className="text-lg sm:text-xl font-semibold text-[#1a3c34] mb-4">
          Xu Hướng Doanh Thu Theo Tháng
        </h2>
        <div className="-ml-2 sm:ml-0">
          <Chart
            options={revenueTrendOptions}
            series={chartData.series}
            type="area"
            height={300}
            width="100%"
          />
        </div>
      </div>
    );
  }
);
RevenueTrendChart.displayName = "RevenueTrendChart";

// 🔹 BẢNG DOANH THU THEO CÔNG TY (Tối ưu responsive)
const CompanyRevenueTable = memo(
  ({
    companies,
    searchTerm,
    onSearchChange,
    sortConfig,
    onSort,
    currentPage,
    itemsPerPage,
    onPageChange,
    onItemsPerPageChange,
    onCompanyClick,
  }: {
    companies: any[];
    searchTerm: string;
    onSearchChange: (value: string) => void;
    sortConfig: { key: string; direction: "asc" | "desc" };
    onSort: (key: string) => void;
    currentPage: number;
    itemsPerPage: number;
    onPageChange: (page: number) => void;
    onItemsPerPageChange: (value: number) => void;
    onCompanyClick: (company: any) => void;
  }) => {
    useWhyDidYouUpdate("CompanyRevenueTable", {
      companies: companies.length,
      searchTerm,
      sortConfig,
      currentPage,
      itemsPerPage,
    });
    const debouncedSearchTerm = useDebounce(searchTerm, 300);

    const filteredCompanies = useMemo(() => {
      if (!debouncedSearchTerm.trim()) return companies;
      const searchLower = debouncedSearchTerm.toLowerCase();
      return companies.filter(
        (company) =>
          (company.name || "").toLowerCase().includes(searchLower) ||
          (company.email || "").toLowerCase().includes(searchLower)
      );
    }, [companies, debouncedSearchTerm]);

    const sortedCompanies = useMemo(() => {
      return [...filteredCompanies].sort((a, b) => {
        if (sortConfig.key === "name") {
          const aVal = a.name || "";
          const bVal = b.name || "";
          return sortConfig.direction === "asc"
            ? aVal.localeCompare(bVal)
            : bVal.localeCompare(aVal);
        }
        if (sortConfig.key === "revenue") {
          const aVal = a.revenue || 0;
          const bVal = b.revenue || 0;
          return sortConfig.direction === "asc" ? aVal - bVal : bVal - aVal;
        }
        if (sortConfig.key === "totalShows") {
          const aVal = a.totalShows || 0;
          const bVal = b.totalShows || 0;
          return sortConfig.direction === "asc" ? aVal - bVal : bVal - aVal;
        }
        return 0;
      });
    }, [filteredCompanies, sortConfig]);

    const totalPages = Math.max(
      1,
      Math.ceil(sortedCompanies.length / itemsPerPage)
    );

    useEffect(() => {
      if (currentPage > totalPages) onPageChange(totalPages);
    }, [currentPage, totalPages, onPageChange]);

    useEffect(() => {
      if (currentPage !== 1) onPageChange(1);
    }, [debouncedSearchTerm]);

    const paginatedCompanies = useMemo(() => {
      const startIndex = (currentPage - 1) * itemsPerPage;
      return sortedCompanies.slice(startIndex, startIndex + itemsPerPage);
    }, [sortedCompanies, currentPage, itemsPerPage]);

    const renderSortIcon = (key: string) => {
      if (sortConfig.key !== key) return null;
      return sortConfig.direction === "asc" ? (
        <SortUpIcon className="inline ml-1" />
      ) : (
        <SortDownIcon className="inline ml-1" />
      );
    };

    const formatCurrency = (value: number) =>
      new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
      }).format(value);

    return (
      <div className="bg-white p-4 sm:p-6 rounded-lg shadow border border-[#16a34a]/20">
        <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-6 gap-4">
          <h2 className="text-lg sm:text-xl font-semibold text-[#1a3c34] whitespace-nowrap">
            Doanh Thu Theo Công Ty
          </h2>
          <input
            type="text"
            placeholder="Tìm kiếm công ty..."
            value={searchTerm}
            onChange={(e) => onSearchChange(e.target.value)}
            className="px-4 py-2 border border-[#16a34a]/30 rounded-lg w-full md:w-72 focus:ring-2 focus:ring-[#16a34a] focus:outline-none text-[#1a3c34] placeholder-[#16a34a]"
          />
        </div>
        <div className="overflow-x-auto -mx-4 sm:mx-0">
          <div className="inline-block min-w-full align-middle px-4 sm:px-0">
            <table className="min-w-full leading-normal">
              <thead>
                <tr className="bg-[#16a34a]/10 border-b-2 border-[#16a34a]/20">
                  <th className="px-3 py-3 sm:px-6 sm:py-4 text-left text-xs sm:text-sm font-semibold text-[#16a34a] uppercase tracking-wider whitespace-nowrap">
                    <button
                      onClick={() => onSort("name")}
                      className="flex items-center group hover:text-[#15803d] transition-colors"
                    >
                      Tên Công Ty {renderSortIcon("name")}
                    </button>
                  </th>
                  <th className="px-3 py-3 sm:px-6 sm:py-4 text-left text-xs sm:text-sm font-semibold text-[#16a34a] uppercase tracking-wider whitespace-nowrap">
                    <button
                      onClick={() => onSort("revenue")}
                      className="flex items-center group hover:text-[#15803d] transition-colors"
                    >
                      Doanh Thu {renderSortIcon("revenue")}
                    </button>
                  </th>
                  <th className="px-3 py-3 sm:px-6 sm:py-4 text-left text-xs sm:text-sm font-semibold text-[#16a34a] uppercase tracking-wider whitespace-nowrap">
                    <button
                      onClick={() => onSort("totalShows")}
                      className="flex items-center group hover:text-[#15803d] transition-colors"
                    >
                      Tổng Số Show {renderSortIcon("totalShows")}
                    </button>
                  </th>
                </tr>
              </thead>
              <tbody>
                {paginatedCompanies.map((company) => (
                  <tr
                    key={company.id}
                    className="hover:bg-[#16a34a]/5 border-b border-[#16a34a]/10 last:border-b-0 transition-colors cursor-pointer group"
                    onClick={() => onCompanyClick(company)}
                  >
                    <td className="px-3 py-3 sm:px-6 sm:py-4 min-w-[160px]">
                      <p className="text-[#1a3c34] font-semibold text-sm sm:text-base group-hover:text-[#16a34a]">
                        {company.name}
                      </p>
                      <p className="text-[#16a34a] text-xs truncate max-w-[150px] sm:max-w-xs">
                        {company.email}
                      </p>
                    </td>
                    <td className="px-3 py-3 sm:px-6 sm:py-4 whitespace-nowrap">
                      <p className="text-[#16a34a] font-bold text-sm sm:text-base">
                        {formatCurrency(company.revenue)}
                      </p>
                    </td>
                    <td className="px-3 py-3 sm:px-6 sm:py-4 whitespace-nowrap">
                      <span className="bg-[#16a34a]/10 text-[#1a3c34] text-xs font-medium px-2.5 py-0.5 rounded-full">
                        {company.totalShows} show
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          onPageChange={onPageChange}
          itemsPerPage={itemsPerPage}
          onItemsPerPageChange={onItemsPerPageChange}
          totalItems={sortedCompanies.length}
        />
      </div>
    );
  }
);
CompanyRevenueTable.displayName = "CompanyRevenueTable";

// 🔹 BẢNG DOANH THU SHOW ĐÃ DIỄN (Tối ưu responsive) - ĐÃ SỬA LỖI
const CompletedShowsTable = memo(
  ({
    shows,
    searchTerm,
    onSearchChange,
    sortConfig,
    onSort,
    currentPage,
    itemsPerPage,
    onPageChange,
    onItemsPerPageChange,
    onShowClick,
  }: {
    shows: any[];
    searchTerm: string;
    onSearchChange: (value: string) => void;
    sortConfig: { key: string; direction: "asc" | "desc" };
    onSort: (key: string) => void;
    currentPage: number;
    itemsPerPage: number;
    onPageChange: (page: number) => void;
    onItemsPerPageChange: (value: number) => void;
    onShowClick: (show: any) => void;
  }) => {
    useWhyDidYouUpdate("CompletedShowsTable", {
      shows: shows.length,
      searchTerm,
      sortConfig,
      currentPage,
      itemsPerPage,
    });
    const debouncedSearchTerm = useDebounce(searchTerm, 300);

    // SỬA LỖI: Đảm bảo chỉ lọc các show có status "Đã diễn" và có dữ liệu hợp lệ
    const completedShows = useMemo(() => {
      return shows.filter((show) => {
        // Kiểm tra show có tồn tại và có status
        if (!show || !show.status) return false;
        return show.status === "Đã diễn";
      });
    }, [shows]);

    // SỬA LỖI: Thêm kiểm tra null/undefined trong hàm filter
    const filteredShows = useMemo(() => {
      if (!debouncedSearchTerm.trim()) return completedShows;
      const searchLower = debouncedSearchTerm.toLowerCase();

      return completedShows.filter((show) => {
        if (!show) return false;

        const nameMatch = (show.name || "").toLowerCase().includes(searchLower);
        const venueMatch = (show.venue || "")
          .toLowerCase()
          .includes(searchLower);
        const artistsMatch = Array.isArray(show.artists)
          ? show.artists.some((artist: string) =>
              (artist || "").toLowerCase().includes(searchLower)
            )
          : false;

        return nameMatch || venueMatch || artistsMatch;
      });
    }, [completedShows, debouncedSearchTerm]);

    // SỬA LỖI: Thêm kiểm tra trong hàm sort
    const sortedShows = useMemo(() => {
      return [...filteredShows].sort((a, b) => {
        if (!a || !b) return 0;

        if (sortConfig.key === "name") {
          const aVal = a.name || "";
          const bVal = b.name || "";
          return sortConfig.direction === "asc"
            ? aVal.localeCompare(bVal)
            : bVal.localeCompare(aVal);
        }
        if (sortConfig.key === "date") {
          const aDate = a.date ? new Date(a.date).getTime() : 0;
          const bDate = b.date ? new Date(b.date).getTime() : 0;
          return sortConfig.direction === "asc" ? aDate - bDate : bDate - aDate;
        }
        if (sortConfig.key === "revenue") {
          const aVal = a.revenue || 0;
          const bVal = b.revenue || 0;
          return sortConfig.direction === "asc" ? aVal - bVal : bVal - aVal;
        }
        if (sortConfig.key === "sellThrough") {
          const aVal = a.sellThrough || 0;
          const bVal = b.sellThrough || 0;
          return sortConfig.direction === "asc" ? aVal - bVal : bVal - aVal;
        }
        return 0;
      });
    }, [filteredShows, sortConfig]);

    const totalPages = Math.max(
      1,
      Math.ceil(sortedShows.length / itemsPerPage)
    );

    useEffect(() => {
      if (currentPage > totalPages) onPageChange(totalPages);
    }, [currentPage, totalPages, onPageChange]);

    useEffect(() => {
      if (currentPage !== 1) onPageChange(1);
    }, [debouncedSearchTerm]);

    const paginatedShows = useMemo(() => {
      const startIndex = (currentPage - 1) * itemsPerPage;
      return sortedShows.slice(startIndex, startIndex + itemsPerPage);
    }, [sortedShows, currentPage, itemsPerPage]);

    const renderSortIcon = (key: string) => {
      if (sortConfig.key !== key) return null;
      return sortConfig.direction === "asc" ? (
        <SortUpIcon className="inline ml-1" />
      ) : (
        <SortDownIcon className="inline ml-1" />
      );
    };

    const formatCurrency = (value: number) =>
      new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
      }).format(value);
    const formatDate = (date: Date) =>
      date
        ? new Intl.DateTimeFormat("vi-VN", {
            day: "2-digit",
            month: "2-digit",
            year: "numeric",
          }).format(date)
        : "";

    return (
      <div className="bg-white p-4 sm:p-6 rounded-lg shadow border border-[#16a34a]/20 mt-6">
        <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-6 gap-4">
          <h2 className="text-lg sm:text-xl font-semibold text-[#1a3c34] whitespace-nowrap">
            Doanh Thu Show Đã Diễn
          </h2>
          <input
            type="text"
            placeholder="Tìm kiếm show..."
            value={searchTerm}
            onChange={(e) => onSearchChange(e.target.value)}
            className="px-4 py-2 border border-[#16a34a]/30 rounded-lg w-full md:w-72 focus:ring-2 focus:ring-[#16a34a] focus:outline-none text-[#1a3c34] placeholder-[#16a34a]"
          />
        </div>

        <div className="overflow-x-auto -mx-4 sm:mx-0">
          <div className="inline-block min-w-full align-middle px-4 sm:px-0">
            <table className="min-w-full leading-normal">
              <thead>
                <tr className="bg-[#16a34a]/10 border-b-2 border-[#16a34a]/20">
                  <th className="px-3 py-3 sm:px-6 sm:py-4 text-left text-xs sm:text-sm font-semibold text-[#16a34a] uppercase tracking-wider whitespace-nowrap">
                    <button
                      onClick={() => onSort("name")}
                      className="flex items-center hover:text-[#15803d] transition-colors"
                    >
                      Tên Show {renderSortIcon("name")}
                    </button>
                  </th>
                  <th className="px-3 py-3 sm:px-6 sm:py-4 text-left text-xs sm:text-sm font-semibold text-[#16a34a] uppercase tracking-wider whitespace-nowrap">
                    <button
                      onClick={() => onSort("date")}
                      className="flex items-center hover:text-[#15803d] transition-colors"
                    >
                      Thời Gian {renderSortIcon("date")}
                    </button>
                  </th>
                  <th className="px-3 py-3 sm:px-6 sm:py-4 text-left text-xs sm:text-sm font-semibold text-[#16a34a] uppercase tracking-wider min-w-[140px]">
                    <button
                      onClick={() => onSort("sellThrough")}
                      className="flex items-center hover:text-[#15803d] transition-colors"
                    >
                      Vé Đã Bán {renderSortIcon("sellThrough")}
                    </button>
                  </th>
                  <th className="px-3 py-3 sm:px-6 sm:py-4 text-left text-xs sm:text-sm font-semibold text-[#16a34a] uppercase tracking-wider whitespace-nowrap">
                    <button
                      onClick={() => onSort("revenue")}
                      className="flex items-center hover:text-[#15803d] transition-colors"
                    >
                      Doanh Thu {renderSortIcon("revenue")}
                    </button>
                  </th>
                </tr>
              </thead>
              <tbody>
                {paginatedShows.length > 0 ? (
                  paginatedShows.map((show) => (
                    <tr
                      key={show.id}
                      className="hover:bg-[#16a34a]/5 border-b border-[#16a34a]/10 last:border-b-0 transition-colors cursor-pointer group"
                      onClick={() => onShowClick(show)}
                    >
                      <td className="px-3 py-3 sm:px-6 sm:py-4 min-w-[160px]">
                        <p className="text-[#1a3c34] font-semibold text-sm sm:text-base group-hover:text-[#16a34a]">
                          {show.name || "Không có tên"}
                        </p>
                        <p className="text-[#16a34a] text-xs truncate max-w-[150px] sm:max-w-xs">
                          {show.venue || "Không có địa điểm"}
                        </p>
                      </td>
                      <td className="px-3 py-3 sm:px-6 sm:py-4 whitespace-nowrap">
                        <p className="text-[#1a3c34] text-sm">
                          {formatDate(show.date)}
                        </p>
                      </td>
                      <td className="px-3 py-3 sm:px-6 sm:py-4 min-w-[140px]">
                        <div className="w-full">
                          <div className="flex justify-between mb-1">
                            <span className="text-xs sm:text-sm text-[#16a34a]">
                              {(show.soldTickets || 0).toLocaleString("vi-VN")}{" "}
                              /{" "}
                              {(show.totalTickets || 0).toLocaleString("vi-VN")}
                            </span>
                            <span className="text-[#16a34a] font-bold text-xs">
                              {(show.sellThrough || 0).toFixed(0)}%
                            </span>
                          </div>
                          <div className="w-full bg-[#16a34a]/20 rounded-full h-2">
                            <div
                              className="bg-[#16a34a] h-2 rounded-full transition-all duration-500"
                              style={{ width: `${show.sellThrough || 0}%` }}
                            ></div>
                          </div>
                        </div>
                      </td>
                      <td className="px-3 py-3 sm:px-6 sm:py-4 whitespace-nowrap">
                        <p className="text-[#16a34a] font-bold text-sm sm:text-base">
                          {formatCurrency(show.revenue || 0)}
                        </p>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td
                      colSpan={4}
                      className="px-3 py-6 text-center text-[#16a34a]"
                    >
                      Không có dữ liệu show nào để hiển thị.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          onPageChange={onPageChange}
          itemsPerPage={itemsPerPage}
          onItemsPerPageChange={onItemsPerPageChange}
          totalItems={sortedShows.length}
        />
      </div>
    );
  }
);
CompletedShowsTable.displayName = "CompletedShowsTable";

// 🔹 COMPONENT DASHBOARD CHÍNH
const Dashboard: React.FC = () => {
  const { shows, companies, artists } = useData();
  const [dateRange, setDateRange] = useState<[Date | null, Date | null]>([
    null,
    null,
  ]);
  const [selectedCompany, setSelectedCompany] = useState("all");
  const [isLoading, setIsLoading] = useState(true);
  const [calendarEvents, setCalendarEvents] = useState<any[]>([]);
  const [activeTab, setActiveTab] = useState("overview");

  // State cho modal
  const [selectedEvent, setSelectedEvent] = useState<any>(null);
  const [selectedCompanyDetail, setSelectedCompanyDetail] = useState<any>(null);
  const [selectedShowDetail, setSelectedShowDetail] = useState<any>(null);
  const [isCompanyModalOpen, setIsCompanyModalOpen] = useState(false);
  const [isShowModalOpen, setIsShowModalOpen] = useState(false);

  const [companySearch, setCompanySearch] = useState("");
  const [companySort, setCompanySort] = useState({
    key: "revenue",
    direction: "desc" as "asc" | "desc",
  });
  const [companyPage, setCompanyPage] = useState(1);
  const [companyItemsPerPage, setCompanyItemsPerPage] = useState(12);

  const [showSearch, setShowSearch] = useState("");
  const [showSort, setShowSort] = useState({
    key: "revenue",
    direction: "desc" as "asc" | "desc",
  });
  const [showPage, setShowPage] = useState(1);
  const [showItemsPerPage, setShowItemsPerPage] = useState(12);

  const [searchShowInShowsTab, setSearchShowInShowsTab] = useState("");
  const [showsTabPage, setShowsTabPage] = useState(1);
  const [showsTabItemsPerPage, setShowsTabItemsPerPage] = useState(12);

  const handleCompanyPageChange = useCallback(
    (page: number) => setCompanyPage(page),
    []
  );
  const handleCompanyItemsPerPageChange = useCallback((value: number) => {
    setCompanyItemsPerPage(value);
    setCompanyPage(1);
  }, []);
  const handleShowPageChange = useCallback(
    (page: number) => setShowPage(page),
    []
  );
  const handleShowItemsPerPageChange = useCallback((value: number) => {
    setShowItemsPerPage(value);
    setShowPage(1);
  }, []);
  const handleShowsTabPageChange = useCallback(
    (page: number) => setShowsTabPage(page),
    []
  );
  const handleShowsTabItemsPerPageChange = useCallback((value: number) => {
    setShowsTabItemsPerPage(value);
    setShowsTabPage(1);
  }, []);

  const handleCompanySearchChange = useCallback(
    (value: string) => setCompanySearch(value),
    []
  );
  const handleShowSearchChange = useCallback(
    (value: string) => setShowSearch(value),
    []
  );

  const handleCompanySort = useCallback((key: string) => {
    setCompanySort((prev) => ({
      key,
      direction: prev.key === key && prev.direction === "desc" ? "asc" : "desc",
    }));
  }, []);
  const handleShowSort = useCallback((key: string) => {
    setShowSort((prev) => ({
      key,
      direction: prev.key === key && prev.direction === "desc" ? "asc" : "desc",
    }));
  }, []);

  // Xử lý click vào công ty
  const handleCompanyClick = useCallback((company: any) => {
    setSelectedCompanyDetail(company);
    setIsCompanyModalOpen(true);
  }, []);

  // Xử lý click vào show
  const handleShowClick = useCallback((show: any) => {
    setSelectedShowDetail(show);
    setIsShowModalOpen(true);
  }, []);

  useEffect(() => {
    // Chỉ chạy khi có dữ liệu và chưa có date range nào được set
    if (
      shows.length > 0 &&
      artists.length > 0 &&
      companies.length > 0 &&
      !dateRange[0]
    ) {
      // Mặc định chọn "Tháng này" khi tải trang lần đầu
      const end = new Date();
      const start = new Date(end.getFullYear(), end.getMonth(), 1);
      setDateRange([start, end]);
    }

    if (shows.length > 0) {
      const events = shows.map((show) => ({
        id: show.id,
        title: show.name,
        start: show.datetime,
        extendedProps: {
          revenue: show.ticketTypes.reduce(
            (total, ticket) => total + ticket.soldQuantity * ticket.price,
            0
          ),
          venue: show.venue,
        },
        color: new Date(show.datetime) < new Date() ? "#6B7280" : "#16a34a",
      }));
      setCalendarEvents(events);
      setIsLoading(false);
    } else if (shows.length === 0 && !isLoading) {
      setIsLoading(false);
    }
  }, [shows, artists, companies]);

  const filteredData = useMemo(() => {
    const [startDate, endDate] = dateRange;
    const now = new Date();

    // SỬA LỖI: Thêm kiểm tra dữ liệu đầu vào
    const filteredShows = shows.filter((show) => {
      if (!show || !show.datetime) return false;

      const showDate = new Date(show.datetime);
      if (startDate && showDate < startDate) return false;
      if (endDate) {
        const endOfDay = new Date(endDate);
        endOfDay.setHours(23, 59, 59, 999);
        if (showDate > endOfDay) return false;
      }
      if (selectedCompany !== "all") {
        return (
          show.artistIds?.some(
            (id) =>
              artists.find((a) => a.id === id)?.companyId === selectedCompany
          ) || false
        );
      }
      return true;
    });

    const showTableData = filteredShows.map((show) => {
      const totalSold =
        show.ticketTypes?.reduce((sum, t) => sum + (t.soldQuantity || 0), 0) ||
        0;

      const totalCapacity =
        show.ticketTypes?.reduce((sum, t) => sum + (t.totalQuantity || 0), 0) ||
        0;

      const revenue =
        show.ticketTypes?.reduce(
          (total, t) => total + (t.soldQuantity || 0) * (t.price || 0),
          0
        ) || 0;

      return {
        id: show.id,
        name: show.name,
        datetime: show.datetime,
        date: new Date(show.datetime),
        venue: show.venue,
        revenue,
        soldTickets: totalSold,
        totalTickets: totalCapacity,
        sellThrough: totalCapacity > 0 ? (totalSold / totalCapacity) * 100 : 0,
        status: new Date(show.datetime) < now ? "Đã diễn" : "Sắp diễn",
        artists:
          show.artistIds
            ?.map((id) => artists.find((a) => a.id === id)?.name)
            .filter(Boolean) || [],
      };
    });

    const companyTableData = companies
      .map((company) => {
        const companyShows = filteredShows.filter((show) =>
          show.artistIds?.some(
            (id) => artists.find((a) => a.id === id)?.companyId === company.id
          )
        );
        const revenue = companyShows.reduce(
          (total, show) =>
            total +
            (show.ticketTypes?.reduce(
              (st, t) => st + (t.soldQuantity || 0) * (t.price || 0),
              0
            ) || 0),
          0
        );
        return {
          id: company.id,
          name: company.companyName,
          email: company.email,
          revenue,
          totalShows: companyShows.length,
        };
      })
      .filter((c) => c.totalShows > 0);

    const stats = showTableData.reduce(
      (acc, item) => {
        acc.totalRevenue += item.revenue;
        acc.totalTicketsSold += item.soldTickets;
        if (item.status === "Đã diễn") acc.pastShows++;
        else acc.upcomingShows++;
        return acc;
      },
      {
        totalRevenue: 0,
        totalTicketsSold: 0,
        totalShows: showTableData.length,
        pastShows: 0,
        upcomingShows: 0,
      }
    );

    const totalCapacity = showTableData.reduce(
      (sum, item) => sum + item.totalTickets,
      0
    );
    stats.averageSellThrough =
      totalCapacity > 0 ? (stats.totalTicketsSold / totalCapacity) * 100 : 0;

    return {
      filteredShows,
      stats,
      tableData: { shows: showTableData, companies: companyTableData },
    };
  }, [shows, artists, companies, dateRange, selectedCompany]);

  if (isLoading)
    return (
      <div className="p-4 text-center text-[#16a34a]">Đang tải dữ liệu...</div>
    );

  const CustomDateInput = React.forwardRef(
    (
      {
        value,
        onClick,
        onClear,
      }: { value: string; onClick: () => void; onClear: () => void },
      ref: React.Ref<HTMLButtonElement>
    ) => {
      const hasValue = value && value !== "Chọn khoảng ngày";
      return (
        <div className="relative w-full md:w-64">
          <button
            className="w-full border border-[#16a34a]/30 rounded-md px-3 py-2 text-sm text-left bg-white focus:ring-2 focus:ring-[#16a34a] focus:outline-none flex justify-between items-center text-[#1a3c34]"
            onClick={onClick}
            ref={ref}
            type="button"
          >
            <span className={hasValue ? "text-[#1a3c34]" : "text-[#16a34a]"}>
              {value}
            </span>
            <span className="text-[#16a34a]">📅</span>
          </button>
          {hasValue && (
            <button
              type="button"
              className="absolute right-10 top-1/2 -translate-y-1/2 p-1 text-[#16a34a] hover:text-[#15803d]"
              onClick={(e) => {
                e.stopPropagation();
                onClear();
              }}
            >
              <CloseIcon className="w-4 h-4" />
            </button>
          )}
        </div>
      );
    }
  );
  CustomDateInput.displayName = "CustomDateInput";

  const datePresets = [
    {
      label: "7 ngày qua",
      getRange: () => {
        const end = new Date();
        const start = new Date();
        start.setDate(end.getDate() - 6);
        return [start, end];
      },
    },
    {
      label: "30 ngày qua",
      getRange: () => {
        const end = new Date();
        const start = new Date();
        start.setDate(end.getDate() - 29);
        return [start, end];
      },
    },
    {
      label: "Tháng này",
      getRange: () => {
        const end = new Date();
        const start = new Date(end.getFullYear(), end.getMonth(), 1);
        return [start, end];
      },
    },
    {
      label: "Tháng trước",
      getRange: () => {
        const now = new Date();
        const start = new Date(now.getFullYear(), now.getMonth() - 1, 1);
        const end = new Date(now.getFullYear(), now.getMonth(), 0);
        return [start, end];
      },
    },
  ];

  const formatCurrency = (value: number) =>
    new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(value);

  const renderTabContent = () => {
    switch (activeTab) {
      case "overview":
        return (
          <>
            <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-3 sm:gap-4 mb-8">
              {[
                {
                  label: "Tổng Doanh Thu",
                  value: filteredData.stats.totalRevenue,
                  format: (v: number) =>
                    v >= 1000000000
                      ? `${(v / 1000000000).toFixed(1)}B`
                      : `${(v / 1000000).toFixed(0)}M`,
                  color: "text-[#16a34a]",
                  tooltip: formatCurrency(filteredData.stats.totalRevenue),
                },
                {
                  label: "Vé Đã Bán",
                  value: filteredData.stats.totalTicketsSold,
                  format: (v: number) => v.toLocaleString("vi-VN"),
                  color: "text-[#16a34a]",
                },
                {
                  label: "Tổng Số Show",
                  value: filteredData.stats.totalShows,
                  format: (v: number) => v,
                  color: "text-[#16a34a]",
                },
                {
                  label: "Tỷ Lệ Lấp Đầy",
                  value: filteredData.stats.averageSellThrough,
                  format: (v: number) => `${v.toFixed(1)}%`,
                  color: "text-[#16a34a]",
                },
                {
                  label: "Show Đã Diễn",
                  value: filteredData.stats.pastShows,
                  format: (v: number) => v,
                  color: "text-[#16a34a]",
                },
                {
                  label: "Sắp Diễn",
                  value: filteredData.stats.upcomingShows,
                  format: (v: number) => v,
                  color: "text-[#16a34a]",
                },
              ].map((stat) => (
                <div
                  key={stat.label}
                  className="bg-white p-3 sm:p-4 rounded-lg shadow border border-[#16a34a]/20"
                  title={stat.tooltip || ""}
                >
                  <h3 className="text-[#16a34a] text-xs sm:text-sm font-medium truncate">
                    {stat.label}
                  </h3>
                  <p
                    className={`text-lg sm:text-xl lg:text-2xl font-bold ${stat.color}`}
                  >
                    {stat.format(stat.value)}
                  </p>
                </div>
              ))}
            </div>
            <CompanyRevenueTable
              companies={filteredData.tableData.companies}
              searchTerm={companySearch}
              onSearchChange={handleCompanySearchChange}
              sortConfig={companySort}
              onSort={handleCompanySort}
              currentPage={companyPage}
              itemsPerPage={companyItemsPerPage}
              onPageChange={handleCompanyPageChange}
              onItemsPerPageChange={handleCompanyItemsPerPageChange}
              onCompanyClick={handleCompanyClick}
            />
            <CompletedShowsTable
              shows={filteredData.tableData.shows}
              searchTerm={showSearch}
              onSearchChange={handleShowSearchChange}
              sortConfig={showSort}
              onSort={handleShowSort}
              currentPage={showPage}
              itemsPerPage={showItemsPerPage}
              onPageChange={handleShowPageChange}
              onItemsPerPageChange={handleShowItemsPerPageChange}
              onShowClick={handleShowClick}
            />
          </>
        );
      case "calendar":
        return (
          <div className="bg-white p-2 sm:p-6 rounded-lg shadow border border-[#16a34a]/20">
            <div className="calendar-responsive-wrapper">
              <FullCalendar
                plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
                headerToolbar={{
                  left: "prev,next",
                  center: "title",
                  right: "dayGridMonth,timeGridDay",
                }}
                initialView="dayGridMonth"
                events={calendarEvents}
                locale={viLocale}
                height="auto"
                contentHeight="auto"
                buttonText={{
                  today: "Hôm nay",
                  month: "Tháng",
                  week: "Tuần",
                  day: "Ngày",
                }}
                eventClick={(info) => {
                  setSelectedEvent({
                    title: info.event.title,
                    revenue: info.event.extendedProps.revenue,
                    venue: info.event.extendedProps.venue,
                    date: info.event.start,
                  });
                }}
              />
            </div>
          </div>
        );
      case "shows":
        const ShowsTab = () => {
          const debouncedSearch = useDebounce(searchShowInShowsTab, 300);

          // SỬA LỖI: Thêm kiểm tra null/undefined trong hàm filter
          const filtered = useMemo(() => {
            if (!debouncedSearch) return filteredData.tableData.shows;
            const lower = debouncedSearch.toLowerCase();

            return filteredData.tableData.shows.filter(
              (s) =>
                (s.name || "").toLowerCase().includes(lower) ||
                (s.venue || "").toLowerCase().includes(lower)
            );
          }, [filteredData.tableData.shows, debouncedSearch]);

          const totalPages = Math.max(
            1,
            Math.ceil(filtered.length / showsTabItemsPerPage)
          );
          const paginated = filtered.slice(
            (showsTabPage - 1) * showsTabItemsPerPage,
            showsTabPage * showsTabItemsPerPage
          );

          return (
            <div className="bg-white p-4 sm:p-6 rounded-lg shadow border border-[#16a34a]/20">
              <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-4 gap-4">
                <h2 className="text-lg sm:text-xl font-semibold text-[#1a3c34]">
                  Danh Sách Toàn Bộ Show
                </h2>
                <input
                  type="text"
                  placeholder="Tìm kiếm show..."
                  value={searchShowInShowsTab}
                  onChange={(e) => setSearchShowInShowsTab(e.target.value)}
                  className="px-3 py-2 border border-[#16a34a]/30 rounded-md w-full md:w-72 focus:ring-2 focus:ring-[#16a34a] focus:outline-none text-[#1a3c34] placeholder-[#16a34a]"
                />
              </div>
              <div className="overflow-x-auto -mx-4 sm:mx-0">
                <div className="inline-block min-w-full align-middle px-4 sm:px-0">
                  <table className="min-w-full">
                    <thead>
                      <tr className="bg-[#16a34a]/10">
                        <th className="px-3 py-3 sm:px-5 border-b-2 border-[#16a34a]/20 text-left text-xs font-semibold text-[#16a34a] uppercase whitespace-nowrap">
                          Tên Show
                        </th>
                        <th className="px-3 py-3 sm:px-5 border-b-2 border-[#16a34a]/20 text-left text-xs font-semibold text-[#16a34a] uppercase whitespace-nowrap">
                          Thời gian & Địa điểm
                        </th>
                        <th className="px-3 py-3 sm:px-5 border-b-2 border-[#16a34a]/20 text-left text-xs font-semibold text-[#16a34a] uppercase whitespace-nowrap">
                          Trạng Thái
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {paginated.length > 0 ? (
                        paginated.map((show) => (
                          <tr
                            key={show.id}
                            className="hover:bg-[#16a34a]/5 border-b border-[#16a34a]/10 last:border-b-0 transition-colors cursor-pointer group"
                            onClick={() => handleShowClick(show)}
                          >
                            <td className="px-3 py-3 sm:px-5 border-b bg-white text-sm min-w-[150px]">
                              <p className="font-semibold text-[#1a3c34] group-hover:text-[#16a34a]">
                                {show.name || "Không có tên"}
                              </p>
                            </td>
                            <td className="px-3 py-3 sm:px-5 border-b bg-white text-sm min-w-[180px]">
                              <p className="text-[#1a3c34]">
                                {show.date
                                  ? show.date.toLocaleString("vi-VN")
                                  : "Không có thời gian"}
                              </p>
                              <p className="text-[#16a34a] text-xs truncate max-w-[200px]">
                                {show.venue || "Không có địa điểm"}
                              </p>
                            </td>
                            <td className="px-3 py-3 sm:px-5 border-b bg-white text-sm whitespace-nowrap">
                              <span
                                className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                                  show.status === "Sắp diễn"
                                    ? "bg-[#16a34a]/20 text-[#1a3c34]"
                                    : "bg-gray-100 text-gray-800"
                                }`}
                              >
                                {show.status || "Không xác định"}
                              </span>
                            </td>
                          </tr>
                        ))
                      ) : (
                        <tr>
                          <td
                            colSpan={3}
                            className="px-3 py-6 text-center text-[#16a34a]"
                          >
                            Không tìm thấy show nào phù hợp.
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
              </div>
              <Pagination
                currentPage={showsTabPage}
                totalPages={totalPages}
                onPageChange={handleShowsTabPageChange}
                itemsPerPage={showsTabItemsPerPage}
                onItemsPerPageChange={handleShowsTabItemsPerPageChange}
                totalItems={filtered.length}
              />
            </div>
          );
        };
        return <ShowsTab />;
      case "analytics":
        return (
          <div className="space-y-6">
            <RevenueTrendChart filteredShows={filteredData.tableData.shows} />
          </div>
        );
      default:
        return null;
    }
  };

  return (
    <div className="max-w-7xl mx-auto p-3 sm:p-6 lg:p-8 bg-[#16a34a]/5 min-h-screen rounded-2xl">
      <h1 className="text-2xl md:text-3xl font-bold text-[#1a3c34] mb-6">
        Dashboard Doanh Thu
      </h1>

      <div className="bg-white rounded-lg shadow border border-[#16a34a]/20 mb-6">
        <div className="border-b border-[#16a34a]/20">
          <div className="overflow-x-auto no-scrollbar">
            <nav className="flex space-x-4 sm:space-x-8 px-4 min-w-max">
              {[
                { id: "overview", label: "Tổng Quan" },
                { id: "calendar", label: "Lịch Show" },
                { id: "shows", label: "Danh Sách Show" },
                { id: "analytics", label: "Phân Tích" },
              ].map((tab) => (
                <button
                  key={tab.id}
                  onClick={() => setActiveTab(tab.id)}
                  className={`py-4 px-1 border-b-2 font-medium text-sm whitespace-nowrap transition-colors ${
                    activeTab === tab.id
                      ? "border-[#16a34a] text-[#16a34a]"
                      : "border-transparent text-[#16a34a] hover:text-[#15803d] hover:border-[#16a34a]/30"
                  }`}
                >
                  {tab.label}
                </button>
              ))}
            </nav>
          </div>
        </div>
      </div>

      <div className="bg-white p-4 rounded-lg shadow border border-[#16a34a]/20 mb-6 space-y-4">
        <div className="flex flex-col md:flex-row items-start md:items-center gap-4">
          <div className="flex items-center gap-2 text-[#16a34a] font-medium text-sm whitespace-nowrap w-full md:w-auto">
            <span className="font-semibold">Bộ lọc:</span>
          </div>

          <div className="w-full md:w-auto">
            <DatePicker
              selectsRange
              startDate={dateRange[0]}
              endDate={dateRange[1]}
              onChange={(update: [Date | null, Date | null]) =>
                setDateRange(update)
              }
              locale={vi}
              dateFormat="dd/MM/yyyy"
              customInput={
                <CustomDateInput
                  value={
                    dateRange[0] && dateRange[1]
                      ? `${dateRange[0].toLocaleDateString(
                          "vi-VN"
                        )} - ${dateRange[1].toLocaleDateString("vi-VN")}`
                      : "Chọn khoảng ngày"
                  }
                  onClick={() => {}} // dummy prop
                  onClear={() => setDateRange([null, null])}
                />
              }
              wrapperClassName="w-full"
            />
          </div>

          <div className="w-full md:w-auto">
            <select
              value={selectedCompany}
              onChange={(e) => setSelectedCompany(e.target.value)}
              className="border border-[#16a34a]/30 rounded-md px-3 py-2 text-sm w-full md:w-56 bg-white focus:ring-2 focus:ring-[#16a34a] focus:outline-none text-[#1a3c34]"
            >
              <option value="all">Tất cả công ty</option>
              {companies.map((company) => (
                <option key={company.id} value={company.id}>
                  {company.companyName}
                </option>
              ))}
            </select>
          </div>
        </div>

        <div className="flex flex-wrap items-center gap-2 pt-2 border-t border-[#16a34a]/20 md:border-t-0 md:pt-0">
          <span className="text-sm text-[#16a34a] font-medium hidden sm:inline">
            Chọn nhanh:
          </span>
          {datePresets.map((preset) => (
            <button
              key={preset.label}
              onClick={() => setDateRange(preset.getRange() as [Date, Date])}
              className="px-2.5 py-1 text-xs font-semibold text-[#16a34a] bg-[#16a34a]/10 rounded-full hover:bg-[#16a34a]/20 hover:text-[#15803d] transition-colors"
            >
              {preset.label}
            </button>
          ))}
        </div>
      </div>

      {/* Modal Event Details */}
      {selectedEvent && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl max-w-md w-full transform transition-all border border-[#16a34a]/20">
            <div className="flex justify-between items-center p-6 border-b border-[#16a34a]/20 bg-[#16a34a]/10 rounded-t-xl">
              <h3 className="text-lg font-semibold text-[#1a3c34]">
                Thông Tin Show
              </h3>
              <button
                onClick={() => setSelectedEvent(null)}
                className="text-[#16a34a] hover:text-[#15803d] transition-colors"
              >
                <CloseIcon className="w-5 h-5" />
              </button>
            </div>

            <div className="p-6 space-y-4">
              <div>
                <label className="text-sm font-medium text-[#16a34a]">
                  Tên Show
                </label>
                <p className="text-[#1a3c34] font-semibold mt-1">
                  {selectedEvent.title}
                </p>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="text-sm font-medium text-[#16a34a]">
                    Địa điểm
                  </label>
                  <p className="text-[#1a3c34] mt-1">{selectedEvent.venue}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-[#16a34a]">
                    Thời gian
                  </label>
                  <p className="text-[#1a3c34] mt-1">
                    {new Date(selectedEvent.date).toLocaleDateString("vi-VN", {
                      day: "2-digit",
                      month: "2-digit",
                      year: "numeric",
                      hour: "2-digit",
                      minute: "2-digit",
                    })}
                  </p>
                </div>
              </div>

              <div className="bg-[#16a34a]/10 p-4 rounded-lg border border-[#16a34a]/20">
                <label className="text-sm font-medium text-[#16a34a]">
                  Doanh thu dự kiến
                </label>
                <p className="text-[#1a3c34] font-bold text-lg mt-1">
                  {formatCurrency(selectedEvent.revenue)}
                </p>
              </div>
            </div>

            <div className="flex justify-end p-6 border-t border-[#16a34a]/20 gap-3">
              <button
                onClick={() => setSelectedEvent(null)}
                className="px-4 py-2 text-[#16a34a] bg-[#16a34a]/10 rounded-lg hover:bg-[#16a34a]/20 transition-colors font-medium"
              >
                Đóng
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Modal chi tiết công ty */}
      <CompanyDetailModal
        isOpen={isCompanyModalOpen}
        onClose={() => setIsCompanyModalOpen(false)}
        company={selectedCompanyDetail}
      />

      {/* Modal chi tiết show */}
      <ShowDetailModal
        isOpen={isShowModalOpen}
        onClose={() => setIsShowModalOpen(false)}
        show={selectedShowDetail}
      />

      {filteredData.stats.totalShows === 0 ? (
        <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-8 text-center">
          <div className="text-yellow-500 text-4xl mb-2">📭</div>
          <p className="text-yellow-800 font-medium">
            Không tìm thấy dữ liệu show nào phù hợp với bộ lọc.
          </p>
          <button
            onClick={() => {
              setDateRange([null, null]);
              setSelectedCompany("all");
            }}
            className="mt-4 text-sm text-[#16a34a] hover:underline"
          >
            Xóa tất cả bộ lọc
          </button>
        </div>
      ) : (
        <div className="transition-opacity duration-300 ease-in-out">
          {renderTabContent()}
        </div>
      )}

      <style>{`
        /* Ẩn scrollbar nhưng vẫn cuộn được */
        .no-scrollbar::-webkit-scrollbar {
          display: none;
        }
        .no-scrollbar {
          -ms-overflow-style: none;
          scrollbar-width: none;
        }
        /* FullCalendar responsive tweaks */
        @media (max-width: 640px) {
            .fc-toolbar {
                flex-direction: column;
                gap: 0.5rem;
            }
            .fc-toolbar-title {
                font-size: 1.1rem !important;
            }
        }
        .animate-scale-in {
          animation: scaleIn 0.2s ease-out forwards;
        }
        @keyframes scaleIn {
          from {
            transform: scale(0.95);
            opacity: 0;
          }
          to {
            transform: scale(1);
            opacity: 1;
          }
        }
      `}</style>
    </div>
  );
};

export default Dashboard;
