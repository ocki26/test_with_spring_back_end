import React, { useState, useEffect, useMemo } from "react";
import { useData } from "../../contexts/DataContext";
import type { Show, TicketType, Artist, Company } from "../../types";
import {
  EditIcon,
  DeleteIcon,
  CloseIcon,
  SortUpIcon,
  SortDownIcon,
} from "../../components/Icons";

// 🔹 ADD ICON COMPONENT
const AddIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    className="h-5 w-5"
    viewBox="0 0 20 20"
    fill="currentColor"
  >
    <path
      fillRule="evenodd"
      d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z"
      clipRule="evenodd"
    />
  </svg>
);

// 🔹 SHOW DETAIL MODAL - Modal hiển thị thông tin đầy đủ với nút Edit
const ShowDetailModal: React.FC<{
  isOpen: boolean;
  onClose: () => void;
  onEdit: () => void;
  onDelete: () => void;
  show: Show | null;
  artists: Artist[];
  companies: Company[];
}> = ({ isOpen, onClose, onEdit, onDelete, show, artists, companies }) => {
  if (!isOpen || !show) return null;

  // Lấy thông tin ca sĩ
  const showArtists = show.artistIds
    .map((id) => {
      const artist = artists.find((a) => a.id === id);
      if (!artist) return null;
      const company = companies.find((c) => c.id === artist.companyId);
      return {
        ...artist,
        companyName: company?.companyName || "Không rõ",
      };
    })
    .filter((a): a is Artist & { companyName: string } => !!a);

  // Tính toán thống kê
  const totalSold = show.ticketTypes.reduce(
    (sum, ticket) => sum + ticket.soldQuantity,
    0
  );
  const totalCapacity = show.ticketTypes.reduce(
    (sum, ticket) => sum + ticket.totalQuantity,
    0
  );
  const totalRevenue = show.ticketTypes.reduce(
    (sum, ticket) => sum + ticket.soldQuantity * ticket.price,
    0
  );
  const sellThrough = totalCapacity > 0 ? (totalSold / totalCapacity) * 100 : 0;

  const status =
    new Date(show.datetime) < new Date() ? "Đã diễn ra" : "Sắp diễn ra";
  const statusColor =
    status === "Sắp diễn ra"
      ? "bg-green-100 text-green-800"
      : "bg-gray-100 text-gray-800";

  const formatCurrency = (value: number) =>
    new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(value);

  const formatDate = (dateString: string) =>
    new Date(dateString).toLocaleString("vi-VN", {
      weekday: "long",
      year: "numeric",
      month: "long",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });

  return (
    <div className="fixed inset-0 bg-black bg-opacity-60 z-50 flex justify-center items-center p-3 transition-opacity">
      <div className="bg-white rounded-xl shadow-2xl w-full max-w-2xl max-h-[85vh] flex flex-col animate-scale-in">
        {/* Header - Giảm padding và kích thước font */}
        <div className="flex justify-between items-center p-4 border-b border-green-100 bg-green-50 rounded-t-xl">
          <div className="flex items-center gap-2">
            <div className="w-10 h-10 bg-green-500 rounded-lg flex items-center justify-center">
              <span className="text-white font-bold text-base">🎭</span>
            </div>
            <div>
              <h2 className="text-lg font-bold text-green-900">{show.name}</h2>
              <p className="text-green-600 text-xs">
                Thông tin chi tiết show diễn
              </p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="text-green-500 hover:text-green-700 p-1 hover:bg-green-100 rounded-lg transition-colors"
          >
            <CloseIcon />
          </button>
        </div>

        {/* Content - Giảm padding và kích thước các phần tử */}
        <div className="overflow-y-auto p-4 space-y-4">
          {/* Thông tin cơ bản */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-3">
              <div className="flex items-center gap-2 p-3 bg-green-50 rounded-lg border border-green-100">
                <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                  <span className="text-green-600 text-sm">📅</span>
                </div>
                <div>
                  <p className="text-xs font-medium text-green-700">
                    Thời gian
                  </p>
                  <p className="text-green-900 font-semibold text-sm">
                    {formatDate(show.datetime)}
                  </p>
                </div>
              </div>

              <div className="flex items-center gap-2 p-3 bg-green-50 rounded-lg border border-green-100">
                <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                  <span className="text-green-600 text-sm">🏟️</span>
                </div>
                <div>
                  <p className="text-xs font-medium text-green-700">Địa điểm</p>
                  <p className="text-green-900 font-semibold text-sm">
                    {show.location}
                  </p>
                </div>
              </div>
            </div>

            <div className="space-y-3">
              <div className="flex items-center gap-2 p-3 bg-green-50 rounded-lg border border-green-100">
                <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                  <span className="text-green-600 text-sm">👥</span>
                </div>
                <div>
                  <p className="text-xs font-medium text-green-700">Số ca sĩ</p>
                  <p className="text-green-900 font-semibold text-base">
                    {showArtists.length} ca sĩ
                  </p>
                </div>
              </div>

              <div className="flex items-center gap-2 p-3 bg-green-50 rounded-lg border border-green-100">
                <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                  <span className="text-green-600 text-sm">🏷️</span>
                </div>
                <div>
                  <p className="text-xs font-medium text-green-700">
                    Trạng thái
                  </p>
                  <span
                    className={`px-2 py-1 text-xs font-semibold rounded-full ${statusColor}`}
                  >
                    {status}
                  </span>
                </div>
              </div>
            </div>
          </div>

          {/* Danh sách ca sĩ - Giảm kích thước */}
          <div className="border border-green-100 rounded-lg bg-white">
            <div className="p-3 border-b border-green-100 bg-green-50 rounded-t-lg">
              <h3 className="font-semibold text-green-900 text-sm flex items-center gap-2">
                <span>🎤</span>
                Danh sách ca sĩ ({showArtists.length})
              </h3>
            </div>
            <div className="max-h-32 overflow-y-auto">
              {showArtists.length > 0 ? (
                <div className="divide-y divide-green-50">
                  {showArtists.map((artist, index) => (
                    <div
                      key={artist.id}
                      className="p-2 hover:bg-green-50 transition-colors"
                    >
                      <div className="flex justify-between items-center">
                        <div className="flex items-center gap-2">
                          <div className="w-6 h-6 bg-green-100 rounded-full flex items-center justify-center">
                            <span className="text-green-600 text-xs font-medium">
                              {index + 1}
                            </span>
                          </div>
                          <div>
                            <p className="font-medium text-green-900 text-sm">
                              {artist.name}
                            </p>
                            <p className="text-green-600 text-xs">
                              {artist.companyName}
                            </p>
                          </div>
                        </div>
                        <span className="px-1.5 py-0.5 bg-green-100 text-green-700 text-xs rounded-full font-medium">
                          Ca sĩ
                        </span>
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="p-4 text-center">
                  <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-2">
                    <span className="text-green-400 text-xl">🎤</span>
                  </div>
                  <p className="text-green-600 font-medium text-sm">
                    Chưa có ca sĩ nào
                  </p>
                </div>
              )}
            </div>
          </div>

          {/* Thống kê doanh thu - Giảm kích thước */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
            <div className="bg-blue-50 p-3 rounded-lg border border-blue-100">
              <p className="text-blue-700 text-xs font-medium">
                Tổng vé đã bán
              </p>
              <p className="text-blue-900 font-bold text-lg">
                {totalSold.toLocaleString("vi-VN")}
              </p>
              <p className="text-blue-600 text-xs">
                / {totalCapacity.toLocaleString("vi-VN")} vé
              </p>
            </div>

            <div className="bg-green-50 p-3 rounded-lg border border-green-100">
              <p className="text-green-700 text-xs font-medium">
                Tỷ lệ lấp đầy
              </p>
              <p className="text-green-900 font-bold text-lg">
                {sellThrough.toFixed(1)}%
              </p>
              <div className="w-full bg-green-200 rounded-full h-1.5 mt-1.5">
                <div
                  className="bg-green-500 h-1.5 rounded-full transition-all duration-500"
                  style={{ width: `${sellThrough}%` }}
                ></div>
              </div>
            </div>

            <div className="bg-purple-50 p-3 rounded-lg border border-purple-100">
              <p className="text-purple-700 text-xs font-medium">
                Tổng doanh thu
              </p>
              <p className="text-purple-900 font-bold text-lg">
                {formatCurrency(totalRevenue)}
              </p>
            </div>
          </div>

          {/* Chi tiết loại vé - Giảm kích thước */}
          <div className="border border-green-100 rounded-lg bg-white">
            <div className="p-3 border-b border-green-100 bg-green-50 rounded-t-lg">
              <h3 className="font-semibold text-green-900 text-sm flex items-center gap-2">
                <span>🎫</span>
                Chi tiết loại vé ({show.ticketTypes.length})
              </h3>
            </div>
            <div className="overflow-x-auto">
              <table className="min-w-full text-sm">
                <thead>
                  <tr className="bg-green-25 border-b border-green-100">
                    <th className="px-3 py-2 text-left text-xs font-semibold text-green-700 uppercase">
                      Loại vé
                    </th>
                    <th className="px-3 py-2 text-left text-xs font-semibold text-green-700 uppercase">
                      Giá vé
                    </th>
                    <th className="px-3 py-2 text-left text-xs font-semibold text-green-700 uppercase">
                      Đã bán
                    </th>
                    <th className="px-3 py-2 text-left text-xs font-semibold text-green-700 uppercase">
                      Tổng SL
                    </th>
                    <th className="px-3 py-2 text-left text-xs font-semibold text-green-700 uppercase">
                      Doanh thu
                    </th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-green-50">
                  {show.ticketTypes.map((ticket) => {
                    const ticketRevenue = ticket.soldQuantity * ticket.price;
                    const ticketSellThrough =
                      ticket.totalQuantity > 0
                        ? (ticket.soldQuantity / ticket.totalQuantity) * 100
                        : 0;

                    return (
                      <tr
                        key={ticket.id}
                        className="hover:bg-green-25 transition-colors"
                      >
                        <td className="px-3 py-2 text-xs font-medium text-green-900">
                          {ticket.name}
                        </td>
                        <td className="px-3 py-2 text-xs text-green-700">
                          {formatCurrency(ticket.price)}
                        </td>
                        <td className="px-3 py-2 text-xs text-green-700">
                          {ticket.soldQuantity.toLocaleString("vi-VN")}
                        </td>
                        <td className="px-3 py-2 text-xs text-green-700">
                          {ticket.totalQuantity.toLocaleString("vi-VN")}
                        </td>
                        <td className="px-3 py-2 text-xs font-semibold text-green-900">
                          {formatCurrency(ticketRevenue)}
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        </div>

        {/* Footer với nút Edit và Delete - Giảm kích thước */}
        <div className="flex justify-between items-center p-4 border-t border-green-100 bg-green-50 rounded-b-xl">
          <button
            onClick={onDelete}
            className="px-3 py-1.5 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors font-medium flex items-center gap-1.5 text-sm"
          >
            <DeleteIcon />
            Xóa Show
          </button>
          <div className="flex gap-2">
            <button
              onClick={onClose}
              className="px-3 py-1.5 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition-colors font-medium text-sm"
            >
              Đóng
            </button>
            <button
              onClick={onEdit}
              className="px-4 py-1.5 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors font-medium flex items-center gap-1.5 text-sm"
            >
              <EditIcon />
              Chỉnh sửa
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

// --- CÁC COMPONENT KHÁC GIỮ NGUYÊN ---
const emptyShow: Omit<Show, "id"> = {
  name: "",
  datetime: "",
  location: "",
  artistIds: [],
  ticketTypes: [],
};

type SortKey = "name" | "datetime" | "soldPercentage" | "revenue";
type SortDirection = "ascending" | "descending";
const ITEMS_PER_PAGE = 10;

const ConfirmationModal: React.FC<{
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
  title: string;
  message: string;
}> = ({ isOpen, onClose, onConfirm, title, message }) => {
  if (!isOpen) return null;
  return (
    <div className="fixed inset-0 bg-black bg-opacity-60 z-50 flex justify-center items-center p-4 transition-opacity duration-300">
      <div className="bg-white rounded-lg p-6 w-full max-w-md shadow-xl transform transition-all duration-300 scale-95 animate-scale-in">
        <h2 className="text-xl font-bold text-gray-900">{title}</h2>
        <p className="text-gray-700 my-4">{message}</p>
        <div className="flex justify-end gap-3 sm:gap-4 mt-6">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-gray-200 text-gray-800 rounded-md font-semibold hover:bg-gray-300 transition-colors"
          >
            Hủy
          </button>
          <button
            onClick={onConfirm}
            className="px-4 py-2 bg-red-600 text-white rounded-md font-semibold hover:bg-red-700 transition-colors"
          >
            Xác nhận Xóa
          </button>
        </div>
      </div>
    </div>
  );
};

const ShowModal: React.FC<{
  isOpen: boolean;
  onClose: () => void;
  onSave: (show: Omit<Show, "id"> | Show) => void;
  show: Show | null;
}> = ({ isOpen, onClose, onSave, show }) => {
  const { artists, companies } = useData();
  const [formData, setFormData] = useState<Omit<Show, "id"> | Show>(
    show || emptyShow
  );
  const [artistSearchTerm, setArtistSearchTerm] = useState("");
  const isNewShow = !show;

  const selectedArtists = useMemo(() => {
    return formData.artistIds
      .map((id) => {
        const artist = artists.find((a) => a.id === id);
        if (!artist) return null;
        const company = companies.find((c) => c.id === artist.companyId);
        return {
          ...artist,
          companyName: company?.companyName || "Không rõ",
        };
      })
      .filter((a): a is Artist & { companyName: string } => !!a);
  }, [formData.artistIds, artists, companies]);

  const availableArtists = useMemo(() => {
    const lowercasedFilter = artistSearchTerm.toLowerCase();
    return artists
      .filter((artist) => !formData.artistIds.includes(artist.id))
      .filter((artist) => artist.name.toLowerCase().includes(lowercasedFilter))
      .map((artist) => {
        const company = companies.find((c) => c.id === artist.companyId);
        return {
          ...artist,
          companyName: company?.companyName || "Không rõ",
        };
      });
  }, [artists, companies, formData.artistIds, artistSearchTerm]);

  useEffect(() => {
    if (isOpen) {
      setFormData(
        show
          ? JSON.parse(JSON.stringify(show))
          : { ...emptyShow, ticketTypes: [] }
      );
      setArtistSearchTerm("");
    }
  }, [show, isOpen]);

  if (!isOpen) return null;

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleAddArtist = (artistId: string) => {
    setFormData((prev) => ({
      ...prev,
      artistIds: [...prev.artistIds, artistId],
    }));
  };

  const handleRemoveArtist = (artistId: string) => {
    setFormData((prev) => ({
      ...prev,
      artistIds: prev.artistIds.filter((id) => id !== artistId),
    }));
  };

  const handleTicketChange = (
    ticketIndex: number,
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    const updatedTickets = [...formData.ticketTypes];
    let finalValue: string | number = value;
    if (name === "price" || name.includes("Quantity")) {
      finalValue = parseInt(value.replace(/,/g, ""), 10) || 0;
    }
    updatedTickets[ticketIndex] = {
      ...updatedTickets[ticketIndex],
      [name]: finalValue,
    };
    if (
      name === "totalQuantity" &&
      updatedTickets[ticketIndex].soldQuantity > (finalValue as number)
    ) {
      updatedTickets[ticketIndex].soldQuantity = finalValue as number;
    }
    setFormData((prev) => ({ ...prev, ticketTypes: updatedTickets }));
  };

  const addTicket = () => {
    const newTicket: TicketType = {
      id: `ticket${Date.now()}`,
      name: "Thường",
      price: 0,
      totalQuantity: 0,
      soldQuantity: 0,
    };
    setFormData((prev) => ({
      ...prev,
      ticketTypes: [...prev.ticketTypes, newTicket],
    }));
  };

  const removeTicket = (ticketIndex: number) => {
    setFormData((prev) => ({
      ...prev,
      ticketTypes: prev.ticketTypes.filter((_, i) => i !== ticketIndex),
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSave(formData);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-60 z-50 flex justify-center items-center p-2 sm:p-4">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-4xl max-h-[90vh] flex flex-col">
        <div className="flex justify-between items-center p-4 border-b">
          <h2 className="text-xl sm:text-2xl font-bold text-gray-900">
            {show ? "Chỉnh Sửa Show" : "Thêm Show Mới"}
          </h2>
          <button
            onClick={onClose}
            className="text-gray-500 hover:text-gray-800"
          >
            <CloseIcon />
          </button>
        </div>
        <form onSubmit={handleSubmit} className="p-4 space-y-4 overflow-y-auto">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-800">
                Tên Show
              </label>
              <input
                name="name"
                value={formData.name}
                onChange={handleChange}
                className="mt-1 w-full border-gray-300 rounded-md p-2 shadow-sm"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-800">
                Địa điểm
              </label>
              <input
                name="location"
                value={formData.location}
                onChange={handleChange}
                className="mt-1 w-full border-gray-300 rounded-md p-2 shadow-sm"
                required
              />
            </div>
            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-gray-800">
                Thời gian
              </label>
              <input
                type="datetime-local"
                name="datetime"
                value={
                  formData.datetime ? formData.datetime.substring(0, 16) : ""
                }
                onChange={handleChange}
                className="mt-1 w-full border-gray-300 rounded-md p-2 shadow-sm"
                required
              />
            </div>

            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-gray-800 mb-2">
                Quản lý ca sĩ
              </label>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 p-3 border rounded-lg bg-gray-50">
                <div>
                  <h4 className="font-semibold text-gray-700 text-sm mb-2">
                    Ca sĩ tham gia ({selectedArtists.length})
                  </h4>
                  <div className="bg-white p-2 border rounded-md min-h-[150px] max-h-48 overflow-y-auto custom-scrollbar">
                    {selectedArtists.length > 0 ? (
                      selectedArtists.map((artist) => (
                        <div
                          key={artist.id}
                          className="flex justify-between items-center p-1.5 rounded hover:bg-red-50 group"
                        >
                          <div>
                            <span className="text-sm text-gray-900 block font-medium">
                              {artist.name}
                            </span>
                            <span className="text-xs text-gray-500 block">
                              {artist.companyName}
                            </span>
                          </div>
                          <button
                            type="button"
                            onClick={() => handleRemoveArtist(artist.id)}
                            className="text-red-500 opacity-0 group-hover:opacity-100 transition-opacity p-1"
                          >
                            <DeleteIcon />
                          </button>
                        </div>
                      ))
                    ) : (
                      <p className="text-xs text-gray-500 italic text-center pt-4">
                        Chưa chọn ca sĩ.
                      </p>
                    )}
                  </div>
                </div>
                <div>
                  <h4 className="font-semibold text-gray-700 text-sm mb-2">
                    Thêm ca sĩ
                  </h4>
                  <input
                    type="text"
                    placeholder="Tìm ca sĩ..."
                    value={artistSearchTerm}
                    onChange={(e) => setArtistSearchTerm(e.target.value)}
                    className="w-full px-2 py-1.5 border rounded-md text-sm mb-2 shadow-sm"
                  />
                  <div className="bg-white p-2 border rounded-md min-h-[150px] max-h-48 overflow-y-auto custom-scrollbar">
                    {availableArtists.length > 0 ? (
                      availableArtists.map((artist) => (
                        <div
                          key={artist.id}
                          className="flex justify-between items-center p-1.5 rounded hover:bg-green-50 group"
                        >
                          <div>
                            <span className="text-sm text-gray-900 block">
                              {artist.name}
                            </span>
                            <span className="text-xs text-gray-500 block">
                              {artist.companyName}
                            </span>
                          </div>
                          <button
                            type="button"
                            onClick={() => handleAddArtist(artist.id)}
                            className="text-green-600 opacity-0 group-hover:opacity-100 transition-opacity p-1"
                          >
                            <AddIcon />
                          </button>
                        </div>
                      ))
                    ) : (
                      <p className="text-xs text-gray-500 italic text-center pt-4">
                        Không tìm thấy ca sĩ.
                      </p>
                    )}
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="space-y-3 border-t pt-4">
            <div className="flex justify-between items-center">
              <h3 className="text-lg font-semibold text-gray-800">Loại vé</h3>
              <button
                type="button"
                onClick={addTicket}
                className="bg-blue-600 text-white px-3 py-1.5 text-sm rounded-md font-semibold hover:bg-blue-700"
              >
                + Thêm vé
              </button>
            </div>
            <div className="space-y-4">
              {formData.ticketTypes.map((ticket, ticketIndex) => (
                <div
                  key={ticket.id}
                  className="p-3 border rounded-lg bg-gray-50 relative"
                >
                  <button
                    type="button"
                    onClick={() => removeTicket(ticketIndex)}
                    className="absolute top-2 right-2 text-red-500 hover:text-red-700"
                  >
                    <DeleteIcon className="w-4 h-4" />
                  </button>
                  <div
                    className={`grid grid-cols-2 lg:grid-cols-${
                      isNewShow ? 3 : 4
                    } gap-x-4 gap-y-3 items-end`}
                  >
                    <div className="col-span-2 sm:col-span-1">
                      <label className="text-xs font-medium text-gray-600">
                        Loại vé
                      </label>
                      <select
                        name="name"
                        value={ticket.name}
                        onChange={(e) => handleTicketChange(ticketIndex, e)}
                        className="w-full border-gray-300 rounded-md p-1.5 text-sm shadow-sm"
                      >
                        <option>Thường</option>
                        <option>VIP</option>
                        <option>Pro VIP</option>
                      </select>
                    </div>
                    <div>
                      <label className="text-xs font-medium text-gray-600">
                        Giá vé (VND)
                      </label>
                      <input
                        type="text"
                        name="price"
                        value={ticket.price.toLocaleString("vi-VN")}
                        onChange={(e) => handleTicketChange(ticketIndex, e)}
                        className="w-full border-gray-300 rounded-md p-1.5 text-sm shadow-sm"
                        required
                      />
                    </div>
                    <div>
                      <label className="text-xs font-medium text-gray-600">
                        Tổng SL
                      </label>
                      <input
                        type="number"
                        min="0"
                        name="totalQuantity"
                        value={ticket.totalQuantity}
                        onChange={(e) => handleTicketChange(ticketIndex, e)}
                        className="w-full border-gray-300 rounded-md p-1.5 text-sm shadow-sm"
                        required
                      />
                    </div>
                    {!isNewShow && (
                      <div>
                        <label className="text-xs font-medium text-gray-600">
                          Đã bán
                        </label>
                        <input
                          type="number"
                          min="0"
                          max={ticket.totalQuantity}
                          name="soldQuantity"
                          value={ticket.soldQuantity}
                          onChange={(e) => handleTicketChange(ticketIndex, e)}
                          className="w-full border-gray-300 rounded-md p-1.5 text-sm shadow-sm"
                          required
                        />
                      </div>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </div>
          <div className="flex justify-end gap-3 sm:gap-4 pt-4 border-t">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 bg-gray-200 text-gray-800 rounded-md font-semibold hover:bg-gray-300 transition-colors"
            >
              Hủy
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-blue-600 text-white rounded-md font-semibold hover:bg-blue-700 transition-colors"
            >
              Lưu
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

// --- COMPONENT CHÍNH MANAGESHOWS ---
const ManageShows: React.FC = () => {
  const { shows, artists, companies, addShow, updateShow, deleteShow } =
    useData();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedShow, setSelectedShow] = useState<Show | null>(null);

  // State cho modal chi tiết
  const [isShowDetailOpen, setIsShowDetailOpen] = useState(false);
  const [selectedShowDetail, setSelectedShowDetail] = useState<Show | null>(
    null
  );

  const [searchTerm, setSearchTerm] = useState("");
  const [sortConfig, setSortConfig] = useState<{
    key: SortKey;
    direction: SortDirection;
  }>({ key: "datetime", direction: "descending" });
  const [currentPage, setCurrentPage] = useState(1);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [showToDelete, setShowToDelete] = useState<string | null>(null);

  const formatCurrency = (value: number) =>
    new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(value);

  const processedShows = useMemo(() => {
    const getArtistNamesString = (artistIds: string[]) => {
      return artistIds
        .map((id) => artists.find((a) => a.id === id)?.name)
        .filter(Boolean)
        .join(", ");
    };
    const augmentedShows = shows.map((show) => {
      let totalSold = 0;
      let totalQuantity = 0;
      const revenue = show.ticketTypes.reduce((total, ticket) => {
        totalSold += ticket.soldQuantity;
        totalQuantity += ticket.totalQuantity;
        return total + ticket.soldQuantity * ticket.price;
      }, 0);
      const soldPercentage =
        totalQuantity > 0 ? (totalSold / totalQuantity) * 100 : 0;
      const status =
        new Date(show.datetime) < new Date() ? "Đã diễn ra" : "Sắp diễn ra";
      const artistNames = getArtistNamesString(show.artistIds);

      return {
        ...show,
        totalSold,
        totalQuantity,
        revenue,
        soldPercentage,
        status,
        artistNames,
      };
    });
    const filteredShows = augmentedShows.filter(
      (show) =>
        show.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        show.location.toLowerCase().includes(searchTerm.toLowerCase()) ||
        show.artistNames.toLowerCase().includes(searchTerm.toLowerCase())
    );
    return filteredShows.sort((a, b) => {
      if (a[sortConfig.key] < b[sortConfig.key]) {
        return sortConfig.direction === "ascending" ? -1 : 1;
      }
      if (a[sortConfig.key] > b[sortConfig.key]) {
        return sortConfig.direction === "ascending" ? 1 : -1;
      }
      return 0;
    });
  }, [shows, artists, searchTerm, sortConfig]);

  const totalPages = Math.ceil(processedShows.length / ITEMS_PER_PAGE);
  const paginatedShows = processedShows.slice(
    (currentPage - 1) * ITEMS_PER_PAGE,
    currentPage * ITEMS_PER_PAGE
  );

  const handleRequestSort = (key: SortKey) => {
    let direction: SortDirection = "ascending";
    if (sortConfig.key === key && sortConfig.direction === "ascending") {
      direction = "descending";
    }
    setSortConfig({ key, direction });
  };

  const renderSortIcon = (key: SortKey) => {
    if (sortConfig.key !== key) return null;
    return sortConfig.direction === "ascending" ? (
      <SortUpIcon className="inline ml-1" />
    ) : (
      <SortDownIcon className="inline ml-1" />
    );
  };

  const handleAdd = () => {
    setSelectedShow(null);
    setIsModalOpen(true);
  };

  const handleEdit = (show: Show) => {
    setSelectedShow(show);
    setIsModalOpen(true);
  };

  const handleDeleteClick = (showId: string) => {
    setShowToDelete(showId);
    setIsDeleteModalOpen(true);
  };

  const confirmDelete = () => {
    if (showToDelete) {
      deleteShow(showToDelete);
    }
    setIsDeleteModalOpen(false);
    setShowToDelete(null);
  };

  const handleSave = (show: Omit<Show, "id"> | Show) => {
    if ("id" in show) {
      updateShow(show);
    } else {
      addShow(show);
    }
    setIsModalOpen(false);
  };

  // Xử lý click vào hàng để hiển thị modal chi tiết
  const handleShowClick = (show: Show) => {
    setSelectedShowDetail(show);
    setIsShowDetailOpen(true);
  };

  // Xử lý Edit từ modal chi tiết
  const handleEditFromDetail = () => {
    if (selectedShowDetail) {
      setIsShowDetailOpen(false);
      setSelectedShow(selectedShowDetail);
      setIsModalOpen(true);
    }
  };

  // Xử lý Delete từ modal chi tiết
  const handleDeleteFromDetail = () => {
    if (selectedShowDetail) {
      setIsShowDetailOpen(false);
      handleDeleteClick(selectedShowDetail.id);
    }
  };

  return (
    <div className="p-2 sm:p-4 lg:p-6">
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-6 gap-4">
        <h1 className="text-2xl md:text-3xl font-bold text-gray-900">
          Quản lý Show diễn
        </h1>
        <div className="w-full md:w-auto flex flex-col sm:flex-row items-stretch gap-2">
          <input
            type="text"
            placeholder="Tìm kiếm show, địa điểm, ca sĩ..."
            value={searchTerm}
            onChange={(e) => {
              setSearchTerm(e.target.value);
              setCurrentPage(1);
            }}
            className="px-3 py-2 border border-gray-300 rounded-md shadow-sm text-sm w-full md:w-72 focus:ring-blue-500 focus:border-blue-500"
          />
          <button
            onClick={handleAdd}
            className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 flex-shrink-0 font-semibold"
          >
            Thêm Show
          </button>
        </div>
      </div>

      <div className="bg-white shadow-md rounded-lg overflow-hidden">
        <div className="overflow-x-auto">
          <table className="min-w-full leading-normal">
            <thead>
              <tr className="bg-gray-50">
                <th className="px-3 py-3 sm:px-5 border-b-2 border-gray-200 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  <button
                    onClick={() => handleRequestSort("name")}
                    className="flex items-center hover:text-gray-900"
                  >
                    Tên Show {renderSortIcon("name")}
                  </button>
                </th>
                <th className="px-3 py-3 sm:px-5 border-b-2 border-gray-200 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider min-w-[200px]">
                  <button
                    onClick={() => handleRequestSort("datetime")}
                    className="flex items-center hover:text-gray-900"
                  >
                    Thời gian & Địa điểm {renderSortIcon("datetime")}
                  </button>
                </th>
                <th className="px-3 py-3 sm:px-5 border-b-2 border-gray-200 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider min-w-[150px]">
                  Ca sĩ
                </th>
                <th className="px-3 py-3 sm:px-5 border-b-2 border-gray-200 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider min-w-[150px]">
                  <button
                    onClick={() => handleRequestSort("soldPercentage")}
                    className="flex items-center hover:text-gray-900"
                  >
                    Vé đã bán {renderSortIcon("soldPercentage")}
                  </button>
                </th>
                <th className="px-3 py-3 sm:px-5 border-b-2 border-gray-200 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  <button
                    onClick={() => handleRequestSort("revenue")}
                    className="flex items-center hover:text-gray-900"
                  >
                    Doanh thu {renderSortIcon("revenue")}
                  </button>
                </th>
                <th className="px-3 py-3 sm:px-5 border-b-2 border-gray-200 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Trạng thái
                </th>
                <th className="px-3 py-3 sm:px-5 border-b-2 border-gray-200 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Hành động
                </th>
              </tr>
            </thead>
            <tbody>
              {paginatedShows.map((show) => {
                return (
                  <tr
                    key={show.id}
                    className="hover:bg-gray-50 cursor-pointer group"
                    onClick={() => handleShowClick(show)}
                  >
                    <td className="px-3 py-4 sm:px-5 border-b border-gray-200 text-sm">
                      <p className="text-gray-900 whitespace-nowrap font-semibold group-hover:text-blue-600">
                        {show.name}
                      </p>
                    </td>
                    <td className="px-3 py-4 sm:px-5 border-b border-gray-200 text-sm">
                      <p className="text-gray-900 whitespace-nowrap">
                        {new Date(show.datetime).toLocaleString("vi-VN")}
                      </p>
                      <p className="text-gray-600 whitespace-nowrap">
                        {show.location}
                      </p>
                    </td>
                    <td className="px-3 py-4 sm:px-5 border-b border-gray-200 text-sm">
                      <p className="text-gray-900 whitespace-nowrap">
                        {show.artistNames}
                      </p>
                    </td>
                    <td className="px-3 py-4 sm:px-5 border-b border-gray-200 text-sm">
                      <div className="w-full">
                        <div className="flex justify-between text-xs mb-1">
                          <span>{`${show.totalSold.toLocaleString(
                            "vi-VN"
                          )} / ${show.totalQuantity.toLocaleString(
                            "vi-VN"
                          )}`}</span>
                          <span className="font-semibold">
                            {show.soldPercentage.toFixed(1)}%
                          </span>
                        </div>
                        <div className="w-full bg-gray-200 rounded-full h-2">
                          <div
                            className="bg-blue-500 h-2 rounded-full"
                            style={{ width: `${show.soldPercentage}%` }}
                            title={`${show.soldPercentage.toFixed(1)}% đã bán`}
                          ></div>
                        </div>
                      </div>
                    </td>
                    <td className="px-3 py-4 sm:px-5 border-b border-gray-200 text-sm">
                      <p className="text-green-600 whitespace-nowrap font-bold">
                        {formatCurrency(show.revenue)}
                      </p>
                    </td>
                    <td className="px-3 py-4 sm:px-5 border-b border-gray-200 text-sm">
                      <span
                        className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${
                          show.status === "Sắp diễn ra"
                            ? "bg-green-100 text-green-800"
                            : "bg-gray-100 text-gray-800"
                        }`}
                      >
                        {show.status}
                      </span>
                    </td>
                    <td
                      className="px-3 py-4 sm:px-5 border-b border-gray-200 text-sm whitespace-nowrap"
                      onClick={(e) => e.stopPropagation()}
                    >
                      <button
                        onClick={() => handleEdit(show)}
                        className="text-blue-600 hover:text-blue-900 mr-3 p-1 rounded-full hover:bg-blue-100"
                      >
                        <EditIcon />
                      </button>
                      <button
                        onClick={() => handleDeleteClick(show.id)}
                        className="text-red-600 hover:text-red-900 p-1 rounded-full hover:bg-red-100"
                      >
                        <DeleteIcon />
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>

        <div className="px-3 py-4 sm:px-5 border-t flex flex-col sm:flex-row items-center justify-between gap-4">
          <span className="text-xs sm:text-sm text-gray-700">
            Hiển thị{" "}
            <span className="font-semibold">{paginatedShows.length}</span> trên
            tổng số{" "}
            <span className="font-semibold">{processedShows.length}</span> mục
          </span>
          <div className="inline-flex">
            <button
              onClick={() => setCurrentPage((p) => Math.max(1, p - 1))}
              disabled={currentPage === 1}
              className="text-sm bg-gray-200 hover:bg-gray-300 text-gray-800 font-semibold py-2 px-4 rounded-l disabled:opacity-50"
            >
              Trước
            </button>
            <span className="text-sm bg-gray-100 text-gray-800 font-semibold py-2 px-4 border-t border-b border-gray-200">
              Trang {currentPage}/{totalPages}
            </span>
            <button
              onClick={() => setCurrentPage((p) => Math.min(p + 1, totalPages))}
              disabled={currentPage >= totalPages}
              className="text-sm bg-gray-200 hover:bg-gray-300 text-gray-800 font-semibold py-2 px-4 rounded-r disabled:opacity-50"
            >
              Sau
            </button>
          </div>
        </div>
      </div>

      {/* Modals */}
      <ShowModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSave={handleSave}
        show={selectedShow}
      />

      <ShowDetailModal
        isOpen={isShowDetailOpen}
        onClose={() => setIsShowDetailOpen(false)}
        onEdit={handleEditFromDetail}
        onDelete={handleDeleteFromDetail}
        show={selectedShowDetail}
        artists={artists}
        companies={companies}
      />

      <ConfirmationModal
        isOpen={isDeleteModalOpen}
        onClose={() => setIsDeleteModalOpen(false)}
        onConfirm={confirmDelete}
        title="Xác nhận Xóa Show"
        message="Bạn có chắc chắn muốn xóa show này? Thao tác này không thể hoàn tác."
      />

      <style jsx global>{`
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
        .custom-scrollbar::-webkit-scrollbar {
          width: 6px;
        }
        .custom-scrollbar::-webkit-scrollbar-track {
          background: #f1f1f1;
          border-radius: 10px;
        }
        .custom-scrollbar::-webkit-scrollbar-thumb {
          background: #888;
          border-radius: 10px;
        }
        .custom-scrollbar::-webkit-scrollbar-thumb:hover {
          background: #555;
        }
        .bg-green-25 {
          background-color: #f0fdf4;
        }
      `}</style>
    </div>
  );
};

export default ManageShows;
