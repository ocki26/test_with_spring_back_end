import React, { useState, useMemo } from "react";
import { useData } from "../../contexts/DataContext";
import { useAuth } from "../../contexts/AuthContext";
import {
  EditIcon,
  DeleteIcon,
  CloseIcon,
  SortUpIcon,
  SortDownIcon,
} from "../../components/Icons";
import type { Artist } from "../../types";

const ArtistModal: React.FC<{
  isOpen: boolean;
  onClose: () => void;
  onSave: (artist: Omit<Artist, "id"> | Artist) => void;
  artist: Artist | null;
}> = ({ isOpen, onClose, onSave, artist }) => {
  const [name, setName] = useState(artist?.name || "");

  React.useEffect(() => {
    if (isOpen) {
      setName(artist?.name || "");
    }
  }, [artist, isOpen]);

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (artist && "id" in artist) {
      // Editing existing artist
      onSave({ ...artist, name });
    } else {
      // Adding new artist
      onSave({ name } as Omit<Artist, "id">);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex justify-center items-center p-4">
      <div className="bg-white rounded-lg p-6 w-full max-w-md">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-2xl font-bold text-gray-900">
            {artist ? "Sửa ca sĩ" : "Thêm ca sĩ"}
          </h2>
          <button
            onClick={onClose}
            className="text-gray-500 hover:text-gray-700"
          >
            <CloseIcon />
          </button>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label
              htmlFor="name"
              className="block text-sm font-medium text-gray-800"
            >
              Tên ca sĩ
            </label>
            <input
              type="text"
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary sm:text-sm"
              required
            />
          </div>
          <div className="flex justify-end gap-4">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 bg-gray-200 rounded-md hover:bg-gray-300"
            >
              Hủy
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-primary text-white rounded-md hover:bg-secondary"
            >
              Lưu
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

const ConfirmationModal: React.FC<{
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
  title: string;
  message: string;
}> = ({ isOpen, onClose, onConfirm, title, message }) => {
  if (!isOpen) return null;
  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex justify-center items-center p-4">
      <div className="bg-white rounded-lg p-6 w-full max-w-sm">
        <h2 className="text-xl font-bold text-gray-900">{title}</h2>
        <p className="text-gray-700 my-4">{message}</p>
        <div className="flex justify-end gap-4 mt-6">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-gray-200 rounded-md hover:bg-gray-300"
          >
            Hủy
          </button>
          <button
            onClick={onConfirm}
            className="px-4 py-2 bg-error text-white rounded-md hover:bg-red-700"
          >
            Xác nhận Xóa
          </button>
        </div>
      </div>
    </div>
  );
};

type SortKey = "name" | "showCount" | "totalRevenue";
type SortDirection = "ascending" | "descending";
const ITEMS_PER_PAGE = 10;

const ManageArtists: React.FC = () => {
  const { artists, shows, addArtist, updateArtist, deleteArtist } = useData();
  const { user } = useAuth();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedArtist, setSelectedArtist] = useState<Artist | null>(null);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [artistToDelete, setArtistToDelete] = useState<string | null>(null);

  const [searchTerm, setSearchTerm] = useState("");
  const [sortConfig, setSortConfig] = useState<{
    key: SortKey;
    direction: SortDirection;
  }>({ key: "name", direction: "ascending" });
  const [currentPage, setCurrentPage] = useState(1);

  const formatCurrency = (value: number) =>
    new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(value);

  // Kiểm tra xem user có tồn tại và là company không
  const isCompanyUser = user && user.role === "COMPANY";

  const processedArtists = useMemo(() => {
    if (!isCompanyUser) return [];

    const companyArtists = artists.filter((a) => a.companyId === user.id);

    const augmentedArtists = companyArtists.map((artist) => {
      const artistShows = shows.filter(
        (show) => show.artistIds && show.artistIds.includes(artist.id)
      );
      const showCount = artistShows.length;

      // Tính toán doanh thu an toàn
      const totalRevenue = artistShows.reduce((total, show) => {
        try {
          const showRevenue = (show.seatingAreas || []).reduce(
            (showTotal, area) => {
              const areaRevenue = (area.ticketTypes || []).reduce(
                (areaTotal, ticket) => {
                  const sold = ticket.soldQuantity || 0;
                  const price = ticket.price || 0;
                  return areaTotal + sold * price;
                },
                0
              );
              return showTotal + areaRevenue;
            },
            0
          );

          // Phân phối doanh thu cho các artist trong show
          const artistCount = Math.max(show.artistIds?.length || 1, 1);
          return total + showRevenue / artistCount;
        } catch (error) {
          console.error("Error calculating revenue for show:", show.id, error);
          return total;
        }
      }, 0);

      return {
        ...artist,
        showCount,
        totalRevenue,
        // Đảm bảo các giá trị số luôn hợp lệ
        name: artist.name || "Không có tên",
      };
    });

    const filteredArtists = augmentedArtists.filter((artist) =>
      artist.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    // Sắp xếp an toàn
    return filteredArtists.sort((a, b) => {
      const aVal = a[sortConfig.key];
      const bVal = b[sortConfig.key];

      if (aVal === undefined || bVal === undefined) return 0;

      if (aVal < bVal) {
        return sortConfig.direction === "ascending" ? -1 : 1;
      }
      if (aVal > bVal) {
        return sortConfig.direction === "ascending" ? 1 : -1;
      }
      return 0;
    });
  }, [artists, shows, user, searchTerm, sortConfig, isCompanyUser]);

  const paginatedArtists = processedArtists.slice(
    (currentPage - 1) * ITEMS_PER_PAGE,
    currentPage * ITEMS_PER_PAGE
  );

  const handleRequestSort = (key: SortKey) => {
    let direction: SortDirection = "ascending";
    if (sortConfig.key === key && sortConfig.direction === "ascending") {
      direction = "descending";
    }
    setSortConfig({ key, direction });
    setCurrentPage(1);
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
    setSelectedArtist(null);
    setIsModalOpen(true);
  };

  const handleEdit = (artist: Artist) => {
    setSelectedArtist(artist);
    setIsModalOpen(true);
  };

  const handleDelete = (artistId: string) => {
    setArtistToDelete(artistId);
    setIsDeleteModalOpen(true);
  };

  const confirmDelete = () => {
    if (artistToDelete) {
      deleteArtist(artistToDelete);
    }
    setIsDeleteModalOpen(false);
    setArtistToDelete(null);
  };

  const handleSave = (artistData: Omit<Artist, "id"> | Artist) => {
    try {
      if ("id" in artistData) {
        // Update artist
        updateArtist(artistData);
      } else {
        // Add new artist
        if (user && user.role === "COMPANY") {
          addArtist({
            ...artistData,
            companyId: user.id,
          });
        }
      }
      setIsModalOpen(false);
      setSelectedArtist(null);
    } catch (error) {
      console.error("Error saving artist:", error);
    }
  };

  // Nếu không phải company user, hiển thị thông báo
  if (!isCompanyUser) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-center">
          <h2 className="text-xl font-semibold text-gray-700 mb-2">
            Không có quyền truy cập
          </h2>
          <p className="text-gray-500">Chỉ công ty mới có thể quản lý ca sĩ.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6 flex-wrap gap-4">
        <h1 className="text-3xl font-bold text-gray-900">Quản lý ca sĩ</h1>
        <div className="flex items-center gap-4">
          <input
            type="text"
            placeholder="Tìm kiếm ca sĩ..."
            value={searchTerm}
            onChange={(e) => {
              setSearchTerm(e.target.value);
              setCurrentPage(1);
            }}
            className="px-3 py-2 border border-gray-300 rounded-md shadow-sm text-sm w-64 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
          />
          <button
            onClick={handleAdd}
            className="bg-primary text-white px-4 py-2 rounded-md hover:bg-secondary transition-colors flex-shrink-0"
          >
            Thêm ca sĩ
          </button>
        </div>
      </div>

      <div className="bg-white shadow rounded-lg overflow-hidden">
        <table className="min-w-full leading-normal">
          <thead>
            <tr>
              <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                <button
                  onClick={() => handleRequestSort("name")}
                  className="flex items-center focus:outline-none"
                >
                  Tên ca sĩ {renderSortIcon("name")}
                </button>
              </th>
              <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                <button
                  onClick={() => handleRequestSort("showCount")}
                  className="flex items-center focus:outline-none"
                >
                  Số show tham gia {renderSortIcon("showCount")}
                </button>
              </th>
              <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                <button
                  onClick={() => handleRequestSort("totalRevenue")}
                  className="flex items-center focus:outline-none"
                >
                  Tổng doanh thu (Ước tính) {renderSortIcon("totalRevenue")}
                </button>
              </th>
              <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                Hành động
              </th>
            </tr>
          </thead>
          <tbody>
            {paginatedArtists.map((artist) => (
              <tr key={artist.id} className="hover:bg-gray-50">
                <td className="px-5 py-4 border-b border-gray-200 bg-white text-sm">
                  <p className="text-gray-900 whitespace-nowrap font-medium">
                    {artist.name}
                  </p>
                </td>
                <td className="px-5 py-4 border-b border-gray-200 bg-white text-sm">
                  <p className="text-gray-900 whitespace-nowrap text-center">
                    {artist.showCount}
                  </p>
                </td>
                <td className="px-5 py-4 border-b border-gray-200 bg-white text-sm">
                  <p className="text-gray-900 whitespace-nowrap">
                    {formatCurrency(artist.totalRevenue)}
                  </p>
                </td>
                <td className="px-5 py-4 border-b border-gray-200 bg-white text-sm">
                  <button
                    onClick={() => handleEdit(artist)}
                    className="text-blue-600 hover:text-blue-900 mr-3 transition-colors"
                    aria-label="Sửa"
                  >
                    <EditIcon />
                  </button>
                  <button
                    onClick={() => handleDelete(artist.id)}
                    className="text-red-600 hover:text-red-900 transition-colors"
                    aria-label="Xóa"
                  >
                    <DeleteIcon />
                  </button>
                </td>
              </tr>
            ))}
            {paginatedArtists.length === 0 && (
              <tr>
                <td colSpan={4} className="text-center py-8 text-gray-500">
                  {searchTerm
                    ? "Không tìm thấy ca sĩ nào."
                    : "Chưa có ca sĩ nào."}
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {processedArtists.length > 0 && (
        <div className="px-5 py-4 bg-white border-t flex flex-col sm:flex-row items-center justify-between rounded-b-lg shadow">
          <span className="text-sm text-gray-700 mb-2 sm:mb-0">
            Hiển thị{" "}
            {Math.min(
              (currentPage - 1) * ITEMS_PER_PAGE + 1,
              processedArtists.length
            )}{" "}
            đến{" "}
            {Math.min(currentPage * ITEMS_PER_PAGE, processedArtists.length)}{" "}
            của {processedArtists.length} mục
          </span>
          <div className="flex space-x-1">
            <button
              onClick={() => setCurrentPage((p) => Math.max(1, p - 1))}
              disabled={currentPage === 1}
              className="px-3 py-1 bg-gray-200 rounded hover:bg-gray-300 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
            >
              Trước
            </button>
            <button
              onClick={() =>
                setCurrentPage((p) =>
                  Math.min(
                    p + 1,
                    Math.ceil(processedArtists.length / ITEMS_PER_PAGE)
                  )
                )
              }
              disabled={currentPage * ITEMS_PER_PAGE >= processedArtists.length}
              className="px-3 py-1 bg-gray-200 rounded hover:bg-gray-300 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
            >
              Sau
            </button>
          </div>
        </div>
      )}

      <ArtistModal
        isOpen={isModalOpen}
        onClose={() => {
          setIsModalOpen(false);
          setSelectedArtist(null);
        }}
        onSave={handleSave}
        artist={selectedArtist}
      />

      <ConfirmationModal
        isOpen={isDeleteModalOpen}
        onClose={() => setIsDeleteModalOpen(false)}
        onConfirm={confirmDelete}
        title="Xác nhận Xóa Ca sĩ"
        message="Bạn có chắc chắn muốn xóa ca sĩ này? Thao tác này không thể hoàn tác."
      />
    </div>
  );
};

export default ManageArtists;
