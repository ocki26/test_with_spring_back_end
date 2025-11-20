import React, { useMemo, useState } from "react";
import { useData } from "../../contexts/DataContext";
import { useAuth } from "../../contexts/AuthContext";
import type { Show } from "../../types";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
} from "recharts";

// Enhanced ShowCard component
const ShowCard: React.FC<{
  show: Show & {
    totalSold: number;
    totalQuantity: number;
    revenue: number;
    soldPercentage: number;
  };
}> = ({ show }) => {
  const { artists } = useData();
  const getArtistNames = (artistIds: string[]) => {
    return artistIds
      .map((id) => artists.find((a) => a.id === id)?.name)
      .filter(Boolean)
      .join(", ");
  };
  const formatCurrency = (value: number) =>
    new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(value);

  return (
    <div className="bg-white rounded-lg shadow-md p-6 flex flex-col justify-between border border-[#16a34a]/20">
      <div>
        <h3 className="text-xl font-bold text-[#1a3c34] mb-2">{show.name}</h3>
        <p className="text-[#1a3c34]">
          {new Date(show.datetime).toLocaleString("vi-VN")}
        </p>
        <p className="text-[#16a34a] mb-3">{show.location}</p>
        <p className="text-sm text-[#1a3c34] mb-4">
          <strong>Ca sĩ:</strong> {getArtistNames(show.artistIds)}
        </p>
      </div>
      <div>
        <div className="flex justify-between text-sm mb-1">
          <span className="font-semibold text-[#1a3c34]">Doanh thu:</span>
          <span className="font-bold text-[#16a34a]">
            {formatCurrency(show.revenue)}
          </span>
        </div>
        <div className="w-full">
          <p className="text-[#1a3c34] whitespace-no-wrap text-right text-sm">{`${show.totalSold.toLocaleString(
            "vi-VN"
          )} / ${show.totalQuantity.toLocaleString("vi-VN")} vé`}</p>
          <div className="w-full bg-[#16a34a]/20 rounded-full h-2.5 mt-1">
            <div
              className="bg-[#16a34a] h-2.5 rounded-full"
              style={{ width: `${show.soldPercentage}%` }}
              title={`${show.soldPercentage.toFixed(1)}% đã bán`}
            ></div>
          </div>
        </div>
      </div>
    </div>
  );
};

const CompanyDashboard: React.FC = () => {
  const { shows, artists } = useData();
  const { user } = useAuth();
  const [searchTerm, setSearchTerm] = useState("");

  const formatCurrency = (value: number) =>
    new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(value);

  const companyData = useMemo(() => {
    if (!user || user.role !== "COMPANY") {
      return {
        companyShows: [],
        totalRevenue: 0,
        totalTicketsSold: 0,
        averageSellThrough: 0,
        showRevenueData: [],
        topArtistsData: [],
        ticketTypeData: [],
      };
    }

    const companyArtists = artists.filter((a) => a.companyId === user.id);
    const companyArtistIds = companyArtists.map((a) => a.id);

    const companyShows = shows
      .filter((show) =>
        show.artistIds.some((artistId) => companyArtistIds.includes(artistId))
      )
      .map((show) => {
        let totalSold = 0;
        let totalQuantity = 0;
        const revenue = show.ticketTypes.reduce((total, ticket) => {
          totalSold += ticket.soldQuantity;
          totalQuantity += ticket.totalQuantity;
          return total + ticket.soldQuantity * ticket.price;
        }, 0);
        const soldPercentage =
          totalQuantity > 0 ? (totalSold / totalQuantity) * 100 : 0;
        return { ...show, totalSold, totalQuantity, revenue, soldPercentage };
      });

    const { totalRevenue, totalTicketsSold, totalTicketCapacity } =
      companyShows.reduce(
        (acc, show) => {
          acc.totalRevenue += show.revenue;
          acc.totalTicketsSold += show.totalSold;
          acc.totalTicketCapacity += show.totalQuantity;
          return acc;
        },
        { totalRevenue: 0, totalTicketsSold: 0, totalTicketCapacity: 0 }
      );

    const averageSellThrough =
      totalTicketCapacity > 0
        ? (totalTicketsSold / totalTicketCapacity) * 100
        : 0;

    const showRevenueData = companyShows
      .map((s) => ({ name: s.name, revenue: s.revenue }))
      .sort((a, b) => b.revenue - a.revenue);

    // Artist Performance Data
    const artistRevenue: { [key: string]: number } = {};
    companyShows.forEach((show) => {
      const artistsInShowFromCompany = show.artistIds.filter((id) =>
        companyArtistIds.includes(id)
      );
      if (artistsInShowFromCompany.length > 0) {
        const revenuePerArtist = show.revenue / artistsInShowFromCompany.length;
        artistsInShowFromCompany.forEach((artistId) => {
          const artist = companyArtists.find((a) => a.id === artistId);
          if (artist) {
            artistRevenue[artist.name] =
              (artistRevenue[artist.name] || 0) + revenuePerArtist;
          }
        });
      }
    });
    const topArtistsData = Object.keys(artistRevenue)
      .map((name) => ({ name, revenue: artistRevenue[name] }))
      .sort((a, b) => b.revenue - a.revenue)
      .slice(0, 5);

    // Ticket Type Sales Data
    const ticketTypeSales: { [key: string]: number } = {};
    companyShows.forEach((show) =>
      show.ticketTypes.forEach((ticket) => {
        ticketTypeSales[ticket.name] =
          (ticketTypeSales[ticket.name] || 0) + ticket.soldQuantity;
      })
    );
    const ticketTypeData = Object.keys(ticketTypeSales)
      .map((name) => ({ name, sold: ticketTypeSales[name] }))
      .filter((item) => item.sold > 0);

    return {
      companyShows,
      totalRevenue,
      totalTicketsSold,
      averageSellThrough,
      showRevenueData,
      topArtistsData,
      ticketTypeData,
    };
  }, [shows, artists, user]);

  const {
    companyShows,
    totalRevenue,
    totalTicketsSold,
    averageSellThrough,
    showRevenueData,
    topArtistsData,
    ticketTypeData,
  } = companyData;

  const filteredShows = useMemo(() => {
    if (!searchTerm) return companyShows;
    const lowercasedFilter = searchTerm.toLowerCase();
    return companyShows.filter((show) => {
      const artistNames = show.artistIds
        .map((id) => artists.find((a) => a.id === id)?.name || "")
        .join(", ");
      return (
        show.name.toLowerCase().includes(lowercasedFilter) ||
        show.location.toLowerCase().includes(lowercasedFilter) ||
        artistNames.toLowerCase().includes(lowercasedFilter)
      );
    });
  }, [companyShows, searchTerm, artists]);

  const now = new Date();
  const upcomingShows = filteredShows
    .filter((show) => new Date(show.datetime) >= now)
    .sort(
      (a, b) => new Date(a.datetime).getTime() - new Date(b.datetime).getTime()
    );
  const pastShows = filteredShows
    .filter((show) => new Date(show.datetime) < now)
    .sort(
      (a, b) => new Date(b.datetime).getTime() - new Date(a.datetime).getTime()
    );
  const COLORS = ["#16a34a", "#15803d", "#166534", "#14532d", "#0f766e"];

  return (
    <div>
      <h1 className="text-3xl font-bold text-[#1a3c34] mb-6">
        Tổng Quan Show Của Tôi
      </h1>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div className="bg-white p-6 rounded-lg shadow border border-[#16a34a]/20">
          <h3 className="text-[#16a34a] text-sm font-medium">Tổng Doanh Thu</h3>
          <p className="text-3xl font-bold text-[#1a3c34]">
            {formatCurrency(totalRevenue)}
          </p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow border border-[#16a34a]/20">
          <h3 className="text-[#16a34a] text-sm font-medium">Tổng Vé Đã Bán</h3>
          <p className="text-3xl font-bold text-[#1a3c34]">
            {totalTicketsSold.toLocaleString("vi-VN")}
          </p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow border border-[#16a34a]/20">
          <h3 className="text-[#16a34a] text-sm font-medium">Tổng Số Show</h3>
          <p className="text-3xl font-bold text-[#1a3c34]">
            {companyShows.length}
          </p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow border border-[#16a34a]/20">
          <h3 className="text-[#16a34a] text-sm font-medium">
            Tỷ Lệ Lấp Đầy (TB)
          </h3>
          <p className="text-3xl font-bold text-[#1a3c34]">
            {averageSellThrough.toFixed(1)}%
          </p>
        </div>
      </div>

      {/* Charts Section */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 mb-8">
        <div className="bg-white p-6 rounded-lg shadow border border-[#16a34a]/20 lg:col-span-2">
          <h2 className="text-xl font-semibold text-[#1a3c34] mb-4">
            Doanh Thu Theo Show
          </h2>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart
              data={showRevenueData}
              margin={{ top: 5, right: 20, left: 20, bottom: 5 }}
            >
              <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
              <XAxis dataKey="name" tick={false} />
              <YAxis
                tickFormatter={(value) =>
                  `${(Number(value) / 1000000).toLocaleString("vi-VN")} Tr`
                }
              />
              <Tooltip formatter={(value: number) => formatCurrency(value)} />
              <Legend />
              <Bar dataKey="revenue" fill="#16a34a" name="Doanh thu" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div className="bg-white p-6 rounded-lg shadow border border-[#16a34a]/20 mb-8">
        <h2 className="text-xl font-semibold text-[#1a3c34] mb-4">
          Top 5 Nghệ Sĩ Theo Doanh Thu
        </h2>
        <ResponsiveContainer width="100%" height={300}>
          <BarChart data={topArtistsData}>
            <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
            <XAxis dataKey="name" tick={{ fontSize: 12 }} />
            <YAxis
              tickFormatter={(value) =>
                `${(Number(value) / 1000000).toLocaleString("vi-VN")} Tr`
              }
            />
            <Tooltip formatter={(value: number) => formatCurrency(value)} />
            <Legend />
            <Bar dataKey="revenue" fill="#16a34a" name="Doanh thu (Ước tính)" />
          </BarChart>
        </ResponsiveContainer>
      </div>

      {/* Show Lists */}
      <div className="mb-4">
        <input
          type="text"
          placeholder="Tìm kiếm show, địa điểm, ca sĩ..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="px-3 py-2 border border-[#16a34a]/30 rounded-md shadow-sm text-sm w-full md:w-1/3 focus:outline-none focus:ring-2 focus:ring-[#16a34a] focus:border-transparent"
        />
      </div>
      <div className="mb-10">
        <h2 className="text-2xl font-semibold text-[#1a3c34] mb-4">
          Show Sắp Diễn Ra
        </h2>
        {upcomingShows.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {upcomingShows.map((show) => (
              <ShowCard key={show.id} show={show} />
            ))}
          </div>
        ) : (
          <div className="bg-white rounded-lg shadow p-6 text-center text-[#16a34a] border border-[#16a34a]/20">
            {searchTerm
              ? "Không tìm thấy show nào phù hợp."
              : "Không có show nào sắp diễn ra."}
          </div>
        )}
      </div>

      <div>
        <h2 className="text-2xl font-semibold text-[#1a3c34] mb-4">
          Show Đã Diễn Ra
        </h2>
        {pastShows.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {pastShows.map((show) => (
              <ShowCard key={show.id} show={show} />
            ))}
          </div>
        ) : (
          <div className="bg-white rounded-lg shadow p-6 text-center text-[#16a34a] border border-[#16a34a]/20">
            {searchTerm
              ? "Không tìm thấy show nào phù hợp."
              : "Chưa có show nào đã diễn ra."}
          </div>
        )}
      </div>
    </div>
  );
};

export default CompanyDashboard;
