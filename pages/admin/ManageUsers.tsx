import React, { useState, useEffect, useMemo } from "react";
import { useData } from "../../contexts/DataContext";
import type { User, Company, Staff, Artist } from "../../types";
import {
  EditIcon,
  DeleteIcon,
  CloseIcon,
  SortUpIcon,
  SortDownIcon,
} from "../../components/Icons";

type SaveableUser =
  | Omit<User, "id" | "password">
  | (Partial<User> & { id: string });
type SortDirection = "ascending" | "descending";
type CompanySortKey = "companyName" | "email" | "artistCount";
type StaffSortKey = "name" | "email";

// --- MODAL CHI TIẾT CÔNG TY VỚI NÚT HÀNH ĐỘNG ---
const CompanyDetailModal: React.FC<{
  isOpen: boolean;
  onClose: () => void;
  onEdit: (company: Company) => void;
  onDelete: (companyId: string) => void;
  company: Company | null;
  artists: Artist[];
}> = ({ isOpen, onClose, onEdit, onDelete, company, artists }) => {
  if (!isOpen || !company) return null;

  const companyArtists = artists.filter((a) => a.companyId === company.id);

  const handleEdit = () => {
    onEdit(company);
    onClose();
  };

  const handleDelete = () => {
    if (
      window.confirm(`Bạn có chắc muốn xóa công ty "${company.companyName}"?`)
    ) {
      onDelete(company.id);
      onClose();
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-60 z-50 flex justify-center items-center p-4 transition-opacity">
      <div className="bg-white rounded-xl shadow-2xl w-full max-w-md max-h-[90vh] flex flex-col animate-scale-in">
        {/* Header */}
        <div className="flex justify-between items-center p-6 border-b border-green-100 bg-green-50 rounded-t-xl">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 bg-green-500 rounded-lg flex items-center justify-center">
              <span className="text-white font-bold text-lg">
                {company.companyName.charAt(0)}
              </span>
            </div>
            <div>
              <h2 className="text-xl font-bold text-green-900">
                {company.companyName}
              </h2>
              <p className="text-green-600 text-sm">Thông tin công ty</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="text-green-500 hover:text-green-700 p-2 hover:bg-green-100 rounded-lg transition-colors"
          >
            <CloseIcon />
          </button>
        </div>

        {/* Content */}
        <div className="overflow-y-auto p-6 space-y-4">
          <div className="space-y-3">
            <div className="flex justify-between items-center p-3 bg-green-50 rounded-lg">
              <span className="text-green-700 font-medium">Email:</span>
              <span className="text-green-900 font-semibold">
                {company.email}
              </span>
            </div>

            <div className="flex justify-between items-center p-3 bg-green-50 rounded-lg">
              <span className="text-green-700 font-medium">Số ca sĩ:</span>
              <span className="text-green-900 font-semibold">
                {companyArtists.length} ca sĩ
              </span>
            </div>

            <div className="flex justify-between items-center p-3 bg-green-50 rounded-lg">
              <span className="text-green-700 font-medium">ID:</span>
              <span className="text-green-900 font-mono text-sm">
                {company.id}
              </span>
            </div>
          </div>

          {/* Danh sách ca sĩ */}
          <div className="border border-green-100 rounded-lg bg-white mt-4">
            <div className="p-3 border-b border-green-100 bg-green-50 rounded-t-lg">
              <h3 className="font-semibold text-green-900">
                Danh sách ca sĩ ({companyArtists.length})
              </h3>
            </div>
            <div className="max-h-48 overflow-y-auto p-2">
              {companyArtists.length > 0 ? (
                <div className="space-y-2">
                  {companyArtists.map((artist, index) => (
                    <div
                      key={artist.id}
                      className="flex items-center gap-3 p-2 bg-white rounded border border-green-100 hover:bg-green-50 transition-colors"
                    >
                      <div className="w-6 h-6 bg-green-100 rounded-full flex items-center justify-center">
                        <span className="text-green-600 text-xs font-medium">
                          {index + 1}
                        </span>
                      </div>
                      <span className="text-green-900 text-sm font-medium">
                        {artist.name}
                      </span>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="text-center py-4 text-green-600">
                  <p className="text-sm">Chưa có ca sĩ nào</p>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Footer với nút hành động */}
        <div className="flex justify-between p-4 border-t border-green-100 bg-green-50 rounded-b-xl">
          <button
            onClick={handleDelete}
            className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors font-medium flex items-center gap-2"
          >
            <DeleteIcon />
            Xóa
          </button>
          <div className="flex gap-2">
            <button
              onClick={onClose}
              className="px-4 py-2 bg-white border border-green-300 text-green-700 rounded-lg hover:bg-green-50 transition-colors font-medium"
            >
              Đóng
            </button>
            <button
              onClick={handleEdit}
              className="px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors font-medium flex items-center gap-2"
            >
              <EditIcon />
              Sửa
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

// --- MODAL CHI TIẾT NHÂN VIÊN VỚI NÚT HÀNH ĐỘNG ---
const StaffDetailModal: React.FC<{
  isOpen: boolean;
  onClose: () => void;
  onEdit: (staff: Staff) => void;
  onDelete: (staffId: string) => void;
  staff: Staff | null;
}> = ({ isOpen, onClose, onEdit, onDelete, staff }) => {
  if (!isOpen || !staff) return null;

  const handleEdit = () => {
    onEdit(staff);
    onClose();
  };

  const handleDelete = () => {
    if (window.confirm(`Bạn có chắc muốn xóa nhân viên "${staff.name}"?`)) {
      onDelete(staff.id);
      onClose();
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-60 z-50 flex justify-center items-center p-4 transition-opacity">
      <div className="bg-white rounded-xl shadow-2xl w-full max-w-md max-h-[90vh] flex flex-col animate-scale-in">
        {/* Header */}
        <div className="flex justify-between items-center p-6 border-b border-green-100 bg-green-50 rounded-t-xl">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 bg-green-500 rounded-full flex items-center justify-center">
              <span className="text-white font-bold text-lg">
                {staff.name.charAt(0)}
              </span>
            </div>
            <div>
              <h2 className="text-xl font-bold text-green-900">{staff.name}</h2>
              <p className="text-green-600 text-sm">Thông tin nhân viên</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="text-green-500 hover:text-green-700 p-2 hover:bg-green-100 rounded-lg transition-colors"
          >
            <CloseIcon />
          </button>
        </div>

        {/* Content */}
        <div className="overflow-y-auto p-6 space-y-4">
          <div className="space-y-3">
            <div className="flex justify-between items-center p-3 bg-green-50 rounded-lg">
              <span className="text-green-700 font-medium">Tên nhân viên:</span>
              <span className="text-green-900 font-semibold">{staff.name}</span>
            </div>

            <div className="flex justify-between items-center p-3 bg-green-50 rounded-lg">
              <span className="text-green-700 font-medium">Email:</span>
              <span className="text-green-900 font-semibold">
                {staff.email}
              </span>
            </div>

            <div className="flex justify-between items-center p-3 bg-green-50 rounded-lg">
              <span className="text-green-700 font-medium">Vai trò:</span>
              <span className="text-green-900 font-semibold">Nhân viên</span>
            </div>

            <div className="flex justify-between items-center p-3 bg-green-50 rounded-lg">
              <span className="text-green-700 font-medium">ID:</span>
              <span className="text-green-900 font-mono text-sm">
                {staff.id}
              </span>
            </div>
          </div>
        </div>

        {/* Footer với nút hành động */}
        <div className="flex justify-between p-4 border-t border-green-100 bg-green-50 rounded-b-xl">
          <button
            onClick={handleDelete}
            className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors font-medium flex items-center gap-2"
          >
            <DeleteIcon />
            Xóa
          </button>
          <div className="flex gap-2">
            <button
              onClick={onClose}
              className="px-4 py-2 bg-white border border-green-300 text-green-700 rounded-lg hover:bg-green-50 transition-colors font-medium"
            >
              Đóng
            </button>
            <button
              onClick={handleEdit}
              className="px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors font-medium flex items-center gap-2"
            >
              <EditIcon />
              Sửa
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

// --- MODAL NGƯỜI DÙNG (Giữ nguyên) ---
const UserModal: React.FC<{
  isOpen: boolean;
  onClose: () => void;
  onSave: (user: SaveableUser, newArtists: string[]) => void;
  user: User | null;
  userType: "COMPANY" | "STAFF";
  artists: Artist[];
  addArtist: (artist: Omit<Artist, "id">) => void;
  deleteArtist: (artistId: string) => void;
}> = ({
  isOpen,
  onClose,
  onSave,
  user,
  userType,
  artists,
  addArtist,
  deleteArtist,
}) => {
  const [formData, setFormData] = useState({
    email: "",
    name: "",
  });
  const [newArtistName, setNewArtistName] = useState("");
  const [pendingArtists, setPendingArtists] = useState<string[]>([]);

  const companyArtists =
    user && user.role === "COMPANY"
      ? artists.filter((a) => a.companyId === user.id)
      : [];

  useEffect(() => {
    if (isOpen) {
      if (user) {
        setFormData({
          email: user.email,
          name:
            user.role === "COMPANY" ? (user as Company).companyName : user.name,
        });
      } else {
        setFormData({ email: "", name: "" });
      }
      setNewArtistName("");
      setPendingArtists([]);
    }
  }, [user, isOpen]);

  if (!isOpen) return null;

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleAddArtist = () => {
    if (newArtistName.trim() && user && user.role === "COMPANY") {
      addArtist({ name: newArtistName.trim(), companyId: user.id });
      setNewArtistName("");
    }
  };

  const handleDeleteArtist = (artistId: string) => {
    if (window.confirm("Bạn có chắc muốn xóa ca sĩ này khỏi công ty?")) {
      deleteArtist(artistId);
    }
  };

  const handlePendingAdd = () => {
    if (newArtistName.trim()) {
      setPendingArtists((prev) => [...prev, newArtistName.trim()]);
      setNewArtistName("");
    }
  };

  const handlePendingDelete = (index: number) => {
    setPendingArtists((prev) => prev.filter((_, i) => i !== index));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    let userData:
      | Omit<User, "id" | "password">
      | (Partial<User> & { id: string });

    if (userType === "COMPANY") {
      userData = {
        role: "COMPANY",
        name: formData.name,
        email: formData.email,
        companyName: formData.name,
      };
    } else {
      userData = { role: "STAFF", name: formData.name, email: formData.email };
    }

    if (user) {
      (userData as Partial<User> & { id: string }).id = user.id;
    }

    onSave(userData, user ? [] : pendingArtists);
  };

  const title = `${user ? "Sửa" : "Thêm"} ${
    userType === "COMPANY" ? "Công ty" : "Nhân viên"
  }`;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-60 z-50 flex justify-center items-center p-4 transition-opacity">
      <div className="bg-white rounded-xl shadow-2xl w-full max-w-md max-h-[90vh] flex flex-col animate-scale-in">
        <div className="flex justify-between items-center p-6 border-b border-green-100 bg-green-50 rounded-t-xl">
          <h2 className="text-xl font-bold text-green-900">{title}</h2>
          <button
            onClick={onClose}
            className="text-green-500 hover:text-green-700 p-2 hover:bg-green-100 rounded-lg transition-colors"
          >
            <CloseIcon />
          </button>
        </div>

        <div className="overflow-y-auto p-6">
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-green-700 mb-2">
                {userType === "COMPANY" ? "Tên công ty" : "Tên nhân viên"}
              </label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                className="block w-full px-4 py-3 border border-green-200 rounded-lg shadow-sm focus:ring-2 focus:ring-green-500 focus:border-green-500 sm:text-sm bg-white transition-colors"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-green-700 mb-2">
                Email
              </label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                className="block w-full px-4 py-3 border border-green-200 rounded-lg shadow-sm focus:ring-2 focus:ring-green-500 focus:border-green-500 sm:text-sm bg-white transition-colors"
                required
              />
            </div>

            {userType === "COMPANY" && (
              <fieldset className="border border-green-200 p-4 rounded-lg bg-green-50">
                <legend className="text-sm font-medium text-green-700 px-2 bg-green-50">
                  Quản lý ca sĩ
                </legend>

                <div className="mt-2 mb-3 p-3 bg-white rounded-lg border border-green-200">
                  <div className="flex gap-2">
                    <input
                      type="text"
                      value={newArtistName}
                      onChange={(e) => setNewArtistName(e.target.value)}
                      placeholder="Nhập tên ca sĩ mới..."
                      className="flex-grow block w-full px-3 py-2 border border-green-200 rounded-lg shadow-sm text-sm focus:ring-green-500 focus:border-green-500 bg-white"
                    />
                    <button
                      type="button"
                      onClick={user ? handleAddArtist : handlePendingAdd}
                      className="px-4 py-2 bg-green-500 text-white rounded-lg text-sm font-medium hover:bg-green-600 transition-colors whitespace-nowrap"
                    >
                      Thêm
                    </button>
                  </div>
                </div>

                <div className="space-y-2 max-h-40 overflow-y-auto pr-1 custom-scrollbar">
                  {user ? (
                    companyArtists.length > 0 ? (
                      companyArtists.map((artist) => (
                        <div
                          key={artist.id}
                          className="flex justify-between items-center bg-white p-3 rounded-lg border border-green-200 shadow-sm hover:bg-green-50 transition-colors"
                        >
                          <span className="text-sm text-green-900 font-medium">
                            {artist.name}
                          </span>
                          <button
                            type="button"
                            onClick={() => handleDeleteArtist(artist.id)}
                            className="text-red-500 hover:text-red-700 p-1 rounded-full hover:bg-red-50 transition-colors"
                          >
                            <DeleteIcon />
                          </button>
                        </div>
                      ))
                    ) : (
                      <p className="text-sm text-green-600 italic text-center py-3 bg-green-25 rounded-lg">
                        Công ty này chưa có ca sĩ.
                      </p>
                    )
                  ) : pendingArtists.length > 0 ? (
                    pendingArtists.map((artistName, index) => (
                      <div
                        key={index}
                        className="flex justify-between items-center bg-white p-3 rounded-lg border border-green-200 shadow-sm hover:bg-green-50 transition-colors"
                      >
                        <span className="text-sm text-green-900 font-medium">
                          {artistName}
                        </span>
                        <button
                          type="button"
                          onClick={() => handlePendingDelete(index)}
                          className="text-red-500 hover:text-red-700 p-1 rounded-full hover:bg-red-50 transition-colors"
                        >
                          <DeleteIcon />
                        </button>
                      </div>
                    ))
                  ) : (
                    <p className="text-sm text-green-600 italic text-center py-3 bg-green-25 rounded-lg">
                      Danh sách ca sĩ trống.
                    </p>
                  )}
                </div>
              </fieldset>
            )}

            <div className="flex justify-end gap-3 pt-4 border-t border-green-100 mt-6">
              <button
                type="button"
                onClick={onClose}
                className="px-4 py-2 bg-white border border-green-300 text-green-700 rounded-lg text-sm font-medium hover:bg-green-50 transition-colors"
              >
                Hủy
              </button>
              <button
                type="submit"
                className="px-4 py-2 bg-green-500 text-white rounded-lg text-sm font-medium hover:bg-green-600 transition-colors"
              >
                Lưu thay đổi
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

// --- MODAL XÁC NHẬN (Giữ nguyên) ---
const ConfirmationModal: React.FC<{
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
  title: string;
  message: string;
}> = ({ isOpen, onClose, onConfirm, title, message }) => {
  if (!isOpen) return null;
  return (
    <div className="fixed inset-0 bg-black bg-opacity-60 z-50 flex justify-center items-center p-4 transition-opacity">
      <div className="bg-white rounded-xl p-6 w-full max-w-sm shadow-2xl animate-scale-in border border-green-100">
        <div className="w-12 h-12 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <DeleteIcon className="w-6 h-6 text-red-500" />
        </div>
        <h2 className="text-xl font-bold text-green-900 text-center">
          {title}
        </h2>
        <p className="text-green-600 my-4 text-sm text-center">{message}</p>
        <div className="flex justify-center gap-3 mt-6">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-green-100 text-green-700 rounded-lg text-sm font-medium hover:bg-green-200 transition-colors"
          >
            Hủy
          </button>
          <button
            onClick={onConfirm}
            className="px-4 py-2 bg-red-500 text-white rounded-lg text-sm font-medium hover:bg-red-600 transition-colors"
          >
            Xác nhận Xóa
          </button>
        </div>
      </div>
    </div>
  );
};

const ITEMS_PER_PAGE = 10;

// --- COMPONENT CHÍNH ---
const ManageUsers: React.FC = () => {
  const {
    users,
    artists,
    addUser,
    updateUser,
    deleteUser,
    addArtist,
    deleteArtist,
  } = useData();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [userType, setUserType] = useState<"COMPANY" | "STAFF">("COMPANY");

  // State cho modal chi tiết
  const [isCompanyDetailOpen, setIsCompanyDetailOpen] = useState(false);
  const [selectedCompanyDetail, setSelectedCompanyDetail] =
    useState<Company | null>(null);

  const [isStaffDetailOpen, setIsStaffDetailOpen] = useState(false);
  const [selectedStaffDetail, setSelectedStaffDetail] = useState<Staff | null>(
    null
  );

  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [userToDelete, setUserToDelete] = useState<string | null>(null);

  const [companySearch, setCompanySearch] = useState("");
  const [staffSearch, setStaffSearch] = useState("");

  const [companySort, setCompanySort] = useState<{
    key: CompanySortKey;
    direction: SortDirection;
  }>({ key: "companyName", direction: "ascending" });
  const [staffSort, setStaffSort] = useState<{
    key: StaffSortKey;
    direction: SortDirection;
  }>({ key: "name", direction: "ascending" });

  const [companyPage, setCompanyPage] = useState(1);
  const [staffPage, setStaffPage] = useState(1);
  const [activeTab, setActiveTab] = useState<"company" | "staff">("company");

  const companies = users.filter((u) => u.role === "COMPANY") as Company[];
  const staff = users.filter((u) => u.role === "STAFF") as Staff[];

  // Logic xử lý dữ liệu
  const processedCompanies = useMemo(() => {
    return companies
      .map((c) => ({
        ...c,
        artistCount: artists.filter((a) => a.companyId === c.id).length,
      }))
      .filter(
        (c) =>
          c.companyName.toLowerCase().includes(companySearch.toLowerCase()) ||
          c.email.toLowerCase().includes(companySearch.toLowerCase())
      )
      .sort((a, b) => {
        const valA = a[companySort.key];
        const valB = b[companySort.key];
        if (valA < valB) return companySort.direction === "ascending" ? -1 : 1;
        if (valA > valB) return companySort.direction === "ascending" ? 1 : -1;
        return 0;
      });
  }, [companies, artists, companySearch, companySort]);

  const processedStaff = useMemo(() => {
    return staff
      .filter(
        (s) =>
          s.name.toLowerCase().includes(staffSearch.toLowerCase()) ||
          s.email.toLowerCase().includes(staffSearch.toLowerCase())
      )
      .sort((a, b) => {
        const valA = a[staffSort.key];
        const valB = b[staffSort.key];
        if (valA < valB) return staffSort.direction === "ascending" ? -1 : 1;
        if (valA > valB) return staffSort.direction === "ascending" ? 1 : -1;
        return 0;
      });
  }, [staff, staffSearch, staffSort]);

  const paginatedCompanies = processedCompanies.slice(
    (companyPage - 1) * ITEMS_PER_PAGE,
    companyPage * ITEMS_PER_PAGE
  );
  const paginatedStaff = processedStaff.slice(
    (staffPage - 1) * ITEMS_PER_PAGE,
    staffPage * ITEMS_PER_PAGE
  );

  const handleRequestSort = (
    table: "company" | "staff",
    key: CompanySortKey | StaffSortKey
  ) => {
    if (table === "company") {
      const isAsc =
        companySort.key === key && companySort.direction === "ascending";
      setCompanySort({
        key: key as CompanySortKey,
        direction: isAsc ? "descending" : "ascending",
      });
    } else {
      const isAsc =
        staffSort.key === key && staffSort.direction === "ascending";
      setStaffSort({
        key: key as StaffSortKey,
        direction: isAsc ? "descending" : "ascending",
      });
    }
  };

  const renderSortIcon = (
    key: CompanySortKey | StaffSortKey,
    config: { key: any; direction: SortDirection }
  ) => {
    if (config.key !== key) return null;
    return config.direction === "ascending" ? (
      <SortUpIcon className="inline ml-1" />
    ) : (
      <SortDownIcon className="inline ml-1" />
    );
  };

  const handleAdd = (type: "COMPANY" | "STAFF") => {
    setSelectedUser(null);
    setUserType(type);
    setIsModalOpen(true);
  };

  const handleEdit = (user: User) => {
    setSelectedUser(user);
    setUserType(user.role as "COMPANY" | "STAFF");
    setIsModalOpen(true);
  };

  const handleDeleteClick = (userId: string) => {
    setUserToDelete(userId);
    setIsDeleteModalOpen(true);
  };

  const confirmDelete = () => {
    if (userToDelete) {
      deleteUser(userToDelete);
    }
    setIsDeleteModalOpen(false);
    setUserToDelete(null);
  };

  const handleSave = (user: SaveableUser, newArtists: string[]) => {
    if ("id" in user && user.id) {
      updateUser(user);
    } else {
      addUser(user as Omit<User, "id" | "password">, newArtists);
    }
    setIsModalOpen(false);
  };

  // Xử lý mở modal chi tiết
  const handleCompanyClick = (company: Company) => {
    setSelectedCompanyDetail(company);
    setIsCompanyDetailOpen(true);
  };

  const handleStaffClick = (staff: Staff) => {
    setSelectedStaffDetail(staff);
    setIsStaffDetailOpen(true);
  };

  // Xử lý hành động từ modal chi tiết
  const handleCompanyEdit = (company: Company) => {
    handleEdit(company);
  };

  const handleCompanyDelete = (companyId: string) => {
    handleDeleteClick(companyId);
  };

  const handleStaffEdit = (staff: Staff) => {
    handleEdit(staff);
  };

  const handleStaffDelete = (staffId: string) => {
    handleDeleteClick(staffId);
  };

  return (
    <div className="p-2 sm:p-4 lg:p-6 bg-[#74c69d] min-h-screen rounded-2xl">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-2xl md:text-3xl font-bold text-green-900 mb-2">
          Quản lý Người dùng
        </h1>
        <p className="text-green-600 mb-6">
          Quản lý công ty đối tác và nhân viên hệ thống
        </p>

        {/* Tab Navigation */}
        <div className="bg-white rounded-xl shadow-sm border border-green-100 mb-6 overflow-hidden">
          <div className="border-b border-green-100 overflow-x-auto no-scrollbar">
            <nav className="-mb-px flex space-x-6 sm:space-x-8 min-w-max px-4">
              <button
                onClick={() => setActiveTab("company")}
                className={`whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm transition-colors ${
                  activeTab === "company"
                    ? "border-green-500 text-green-600"
                    : "border-transparent text-green-500 hover:text-green-700 hover:border-green-300"
                }`}
              >
                📊 Tài khoản Công ty Hợp tác
              </button>
              <button
                onClick={() => setActiveTab("staff")}
                className={`whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm transition-colors ${
                  activeTab === "staff"
                    ? "border-green-500 text-green-600"
                    : "border-transparent text-green-500 hover:text-green-700 hover:border-green-300"
                }`}
              >
                👥 Tài khoản Nhân viên
              </button>
            </nav>
          </div>
        </div>

        {/* Company Tab Content */}
        {activeTab === "company" && (
          <div className="animate-scale-in">
            {/* Controls */}
            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
              <div className="flex flex-col sm:flex-row items-stretch gap-3 w-full sm:w-auto">
                <div className="relative flex-1 sm:w-80">
                  <input
                    type="text"
                    placeholder="Tìm kiếm công ty..."
                    value={companySearch}
                    onChange={(e) => {
                      setCompanySearch(e.target.value);
                      setCompanyPage(1);
                    }}
                    className="w-full pl-10 pr-4 py-3 border border-green-200 rounded-lg bg-white focus:ring-2 focus:ring-green-500 focus:border-green-500 text-sm transition-colors"
                  />
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <span className="text-green-400">🔍</span>
                  </div>
                </div>
                <button
                  onClick={() => handleAdd("COMPANY")}
                  className="bg-green-500 text-white px-6 py-3 rounded-lg hover:bg-green-600 transition-colors font-medium whitespace-nowrap flex items-center gap-2 shadow-sm"
                >
                  <span>+</span>
                  Thêm công ty
                </button>
              </div>
            </div>

            {/* Table */}
            <div className="bg-white shadow-sm rounded-xl overflow-hidden border border-green-100">
              <div className="overflow-x-auto">
                <table className="min-w-full leading-normal">
                  <thead>
                    <tr className="bg-green-50 border-b border-green-100">
                      <th className="px-4 py-4 text-left text-xs font-semibold text-green-700 uppercase tracking-wider min-w-[200px]">
                        <button
                          onClick={() =>
                            handleRequestSort("company", "companyName")
                          }
                          className="flex items-center hover:text-green-900 transition-colors"
                        >
                          Tên công ty{" "}
                          {renderSortIcon("companyName", companySort)}
                        </button>
                      </th>
                      <th className="px-4 py-4 text-left text-xs font-semibold text-green-700 uppercase tracking-wider min-w-[220px]">
                        <button
                          onClick={() => handleRequestSort("company", "email")}
                          className="flex items-center hover:text-green-900 transition-colors"
                        >
                          Email {renderSortIcon("email", companySort)}
                        </button>
                      </th>
                      <th className="px-4 py-4 text-left text-xs font-semibold text-green-700 uppercase tracking-wider min-w-[120px]">
                        <button
                          onClick={() =>
                            handleRequestSort("company", "artistCount")
                          }
                          className="flex items-center hover:text-green-900 transition-colors"
                        >
                          Số ca sĩ {renderSortIcon("artistCount", companySort)}
                        </button>
                      </th>
                      <th className="px-4 py-4 text-left text-xs font-semibold text-green-700 uppercase tracking-wider min-w-[120px] hidden md:table-cell">
                        Hành động
                      </th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-green-50">
                    {paginatedCompanies.map((company) => (
                      <tr
                        key={company.id}
                        className="hover:bg-green-25 transition-colors cursor-pointer group"
                        onClick={() => handleCompanyClick(company)}
                      >
                        <td className="px-4 py-4 text-sm font-medium text-green-900 whitespace-nowrap group-hover:text-green-700">
                          <div className="flex items-center gap-3">
                            <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center group-hover:bg-green-200 transition-colors">
                              <span className="text-green-600 font-bold text-sm">
                                {company.companyName.charAt(0)}
                              </span>
                            </div>
                            {company.companyName}
                          </div>
                        </td>
                        <td className="px-4 py-4 text-sm text-green-700 whitespace-nowrap">
                          {company.email}
                        </td>
                        <td className="px-4 py-4 text-sm text-center sm:text-left">
                          <span className="bg-green-100 text-green-700 text-xs font-medium px-3 py-1 rounded-full">
                            {company.artistCount} ca sĩ
                          </span>
                        </td>
                        <td
                          className="px-4 py-4 text-sm whitespace-nowrap hidden md:table-cell"
                          onClick={(e) => e.stopPropagation()}
                        >
                          <div className="flex gap-1">
                            <button
                              onClick={() => handleEdit(company)}
                              className="text-green-600 hover:text-green-800 p-2 rounded-lg hover:bg-green-100 transition-colors"
                              title="Sửa"
                            >
                              <EditIcon />
                            </button>
                            <button
                              onClick={() => handleDeleteClick(company.id)}
                              className="text-red-500 hover:text-red-700 p-2 rounded-lg hover:bg-red-50 transition-colors"
                              title="Xóa"
                            >
                              <DeleteIcon />
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>

            {/* Pagination */}
            <div className="px-4 py-4 bg-white border-t border-green-100 rounded-b-xl flex flex-col sm:flex-row items-center justify-between gap-3 mt-4">
              <span className="text-sm text-green-700">
                Hiển thị{" "}
                <span className="font-semibold">
                  {Math.min(
                    (companyPage - 1) * ITEMS_PER_PAGE + 1,
                    processedCompanies.length
                  )}
                </span>{" "}
                đến{" "}
                <span className="font-semibold">
                  {Math.min(
                    companyPage * ITEMS_PER_PAGE,
                    processedCompanies.length
                  )}
                </span>{" "}
                của{" "}
                <span className="font-semibold">
                  {processedCompanies.length}
                </span>{" "}
                công ty
              </span>
              <div className="inline-flex gap-2">
                <button
                  onClick={() => setCompanyPage((p) => Math.max(1, p - 1))}
                  disabled={companyPage === 1}
                  className="text-sm bg-white border border-green-300 hover:bg-green-50 text-green-700 font-medium py-2 px-4 rounded-lg disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                >
                  ← Trước
                </button>
                <button
                  onClick={() =>
                    setCompanyPage((p) =>
                      Math.min(
                        p + 1,
                        Math.ceil(processedCompanies.length / ITEMS_PER_PAGE)
                      )
                    )
                  }
                  disabled={
                    companyPage * ITEMS_PER_PAGE >= processedCompanies.length
                  }
                  className="text-sm bg-white border border-green-300 hover:bg-green-50 text-green-700 font-medium py-2 px-4 rounded-lg disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                >
                  Sau →
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Staff Tab Content */}
        {activeTab === "staff" && (
          <div className="animate-scale-in">
            {/* Controls */}
            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
              <div className="flex flex-col sm:flex-row items-stretch gap-3 w-full sm:w-auto">
                <div className="relative flex-1 sm:w-80">
                  <input
                    type="text"
                    placeholder="Tìm kiếm nhân viên..."
                    value={staffSearch}
                    onChange={(e) => {
                      setStaffSearch(e.target.value);
                      setStaffPage(1);
                    }}
                    className="w-full pl-10 pr-4 py-3 border border-green-200 rounded-lg bg-white focus:ring-2 focus:ring-green-500 focus:border-green-500 text-sm transition-colors"
                  />
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <span className="text-green-400">🔍</span>
                  </div>
                </div>
                <button
                  onClick={() => handleAdd("STAFF")}
                  className="bg-green-500 text-white px-6 py-3 rounded-lg hover:bg-green-600 transition-colors font-medium whitespace-nowrap flex items-center gap-2 shadow-sm"
                >
                  <span>+</span>
                  Thêm nhân viên
                </button>
              </div>
            </div>

            <div className="bg-white shadow-sm rounded-xl overflow-hidden border border-green-100">
              <div className="overflow-x-auto">
                <table className="min-w-full leading-normal">
                  <thead>
                    <tr className="bg-green-50 border-b border-green-100">
                      <th className="px-4 py-4 text-left text-xs font-semibold text-green-700 uppercase tracking-wider min-w-[180px]">
                        <button
                          onClick={() => handleRequestSort("staff", "name")}
                          className="flex items-center hover:text-green-900 transition-colors"
                        >
                          Tên nhân viên {renderSortIcon("name", staffSort)}
                        </button>
                      </th>
                      <th className="px-4 py-4 text-left text-xs font-semibold text-green-700 uppercase tracking-wider min-w-[200px]">
                        <button
                          onClick={() => handleRequestSort("staff", "email")}
                          className="flex items-center hover:text-green-900 transition-colors"
                        >
                          Email {renderSortIcon("email", staffSort)}
                        </button>
                      </th>
                      <th className="px-4 py-4 text-left text-xs font-semibold text-green-700 uppercase tracking-wider min-w-[120px] hidden md:table-cell">
                        Hành động
                      </th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-green-50">
                    {paginatedStaff.map((s) => (
                      <tr
                        key={s.id}
                        className="hover:bg-green-25 transition-colors cursor-pointer"
                        onClick={() => handleStaffClick(s)}
                      >
                        <td className="px-4 py-4 text-sm font-medium text-green-900 whitespace-nowrap">
                          <div className="flex items-center gap-3">
                            <div className="w-8 h-8 bg-green-100 rounded-full flex items-center justify-center">
                              <span className="text-green-600 text-xs font-medium">
                                {s.name
                                  .split(" ")
                                  .map((n) => n[0])
                                  .join("")
                                  .toUpperCase()}
                              </span>
                            </div>
                            {s.name}
                          </div>
                        </td>
                        <td className="px-4 py-4 text-sm text-green-700 whitespace-nowrap">
                          {s.email}
                        </td>
                        <td
                          className="px-4 py-4 text-sm whitespace-nowrap hidden md:table-cell"
                          onClick={(e) => e.stopPropagation()}
                        >
                          <div className="flex gap-1">
                            <button
                              onClick={() => handleEdit(s)}
                              className="text-green-600 hover:text-green-800 p-2 rounded-lg hover:bg-green-100 transition-colors"
                            >
                              <EditIcon />
                            </button>
                            <button
                              onClick={() => handleDeleteClick(s.id)}
                              className="text-red-500 hover:text-red-700 p-2 rounded-lg hover:bg-red-50 transition-colors"
                            >
                              <DeleteIcon />
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>

            {/* Pagination */}
            <div className="px-4 py-4 bg-white border-t border-green-100 rounded-b-xl flex flex-col sm:flex-row items-center justify-between gap-3 mt-4">
              <span className="text-sm text-green-700">
                Hiển thị{" "}
                <span className="font-semibold">
                  {Math.min(
                    (staffPage - 1) * ITEMS_PER_PAGE + 1,
                    processedStaff.length
                  )}
                </span>{" "}
                đến{" "}
                <span className="font-semibold">
                  {Math.min(staffPage * ITEMS_PER_PAGE, processedStaff.length)}
                </span>{" "}
                của{" "}
                <span className="font-semibold">{processedStaff.length}</span>{" "}
                nhân viên
              </span>
              <div className="inline-flex gap-2">
                <button
                  onClick={() => setStaffPage((p) => Math.max(1, p - 1))}
                  disabled={staffPage === 1}
                  className="text-sm bg-white border border-green-300 hover:bg-green-50 text-green-700 font-medium py-2 px-4 rounded-lg disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                >
                  ← Trước
                </button>
                <button
                  onClick={() =>
                    setStaffPage((p) =>
                      Math.min(
                        p + 1,
                        Math.ceil(processedStaff.length / ITEMS_PER_PAGE)
                      )
                    )
                  }
                  disabled={staffPage * ITEMS_PER_PAGE >= processedStaff.length}
                  className="text-sm bg-white border border-green-300 hover:bg-green-50 text-green-700 font-medium py-2 px-4 rounded-lg disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                >
                  Sau →
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Modals */}
        <UserModal
          isOpen={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          onSave={handleSave}
          user={selectedUser}
          userType={userType}
          artists={artists}
          addArtist={addArtist}
          deleteArtist={deleteArtist}
        />

        {/* Modal chi tiết công ty với nút hành động */}
        <CompanyDetailModal
          isOpen={isCompanyDetailOpen}
          onClose={() => setIsCompanyDetailOpen(false)}
          onEdit={handleCompanyEdit}
          onDelete={handleCompanyDelete}
          company={selectedCompanyDetail}
          artists={artists}
        />

        {/* Modal chi tiết nhân viên với nút hành động */}
        <StaffDetailModal
          isOpen={isStaffDetailOpen}
          onClose={() => setIsStaffDetailOpen(false)}
          onEdit={handleStaffEdit}
          onDelete={handleStaffDelete}
          staff={selectedStaffDetail}
        />

        <ConfirmationModal
          isOpen={isDeleteModalOpen}
          onClose={() => setIsDeleteModalOpen(false)}
          onConfirm={confirmDelete}
          title="Xác nhận Xóa"
          message="Bạn có chắc chắn muốn xóa người dùng này? Thao tác này không thể hoàn tác."
        />

        <style jsx global>{`
          .no-scrollbar::-webkit-scrollbar {
            display: none;
          }
          .no-scrollbar {
            -ms-overflow-style: none;
            scrollbar-width: none;
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
    </div>
  );
};

export default ManageUsers;
