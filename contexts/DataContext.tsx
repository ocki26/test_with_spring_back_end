import React, { createContext, useState, useContext } from 'react';
import type { User, Artist, Show, Company, Buyer, SoldTicket, TicketType } from '../types';

// --- DATA FOR GENERATORS ---
const VIETNAMESE_LAST_NAMES = ['Nguyễn', 'Trần', 'Lê', 'Phạm', 'Hoàng', 'Huỳnh', 'Vũ', 'Võ', 'Phan', 'Trương', 'Bùi', 'Đặng', 'Đỗ', 'Ngô', 'Hồ', 'Dương'];
const VIETNAMESE_MALE_NAMES = ['An', 'Bảo', 'Bình', 'Chí', 'Cường', 'Dũng', 'Đạt', 'Hiếu', 'Hoàng', 'Huy', 'Khải', 'Khang', 'Khoa', 'Kiên', 'Lâm', 'Long', 'Mạnh', 'Minh', 'Nam', 'Nghĩa', 'Phong', 'Phúc', 'Quân', 'Quang', 'Sơn', 'Tài', 'Thành', 'Thiên', 'Toàn', 'Trung', 'Tuấn', 'Việt'];
const VIETNAMESE_FEMALE_NAMES = ['Anh', 'Ánh', 'Bích', 'Châu', 'Chi', 'Dung', 'Giang', 'Hà', 'Hân', 'Hạnh', 'Hoa', 'Hương', 'Khanh', 'Lan', 'Linh', 'Ly', 'Mai', 'My', 'Nga', 'Ngân', 'Ngọc', 'Nhi', 'Nhung', 'Oanh', 'Phương', 'Quỳnh', 'Thảo', 'Thủy', 'Trà', 'Trang', 'Tú', 'Uyên', 'Vân', 'Yến'];
const COMPANY_PREFIXES = ['Công ty', 'Tập đoàn', 'Hãng thu âm', 'Tổ chức sự kiện', 'Giải trí'];
const COMPANY_SUFFIXES = ['Việt', 'Star', 'Music', 'Pro', 'Entertainment', 'Sài Gòn', 'Hà Nội', 'Galaxy', 'Vina'];
const SHOW_PREFIXES = ['Đêm nhạc', 'Liveshow', 'Concert', 'Tour diễn', 'Gala', 'Festival âm nhạc'];
const SHOW_THEMES = ['Tình Yêu', 'Mùa Thu', 'Rực Rỡ', 'Huyền Thoại', 'Tỏa Sáng', 'Kết Nối', 'Chân Trời'];
const LOCATIONS = [
    'Sân vận động Mỹ Đình, Hà Nội',
    'Nhà hát Hòa Bình, TP.HCM',
    'Cung văn hóa hữu nghị Việt Xô, Hà Nội',
    'Trung tâm Hội nghị Quốc gia, Hà Nội',
    'Sân vận động Quân khu 7, TP.HCM',
    'Nhà hát Lớn, Hà Nội',
    'Cung Thể thao Tiên Sơn, Đà Nẵng',
    'Nhà thi đấu Phú Thọ, TP.HCM'
];

// --- HELPER FUNCTIONS ---
// FIX: Changed to a function declaration to resolve JSX parsing ambiguity in .tsx files.
function getRandomElement<T>(arr: T[]): T {
    return arr[Math.floor(Math.random() * arr.length)];
}
const getRandomNumber = (min: number, max: number): number => Math.floor(Math.random() * (max - min + 1)) + min;
const toSlug = (str: string) => str.toLowerCase().replace(/ /g, '-').replace(/[^\w-]+/g, '');


// --- DATA GENERATORS ---
const generateBuyers = (count: number, startIndex: number): Buyer[] => {
    const buyers: Buyer[] = [];
    for (let i = 0; i < count; i++) {
        const isMale = Math.random() > 0.5;
        const lastName = getRandomElement(VIETNAMESE_LAST_NAMES);
        const firstName = isMale ? getRandomElement(VIETNAMESE_MALE_NAMES) : getRandomElement(VIETNAMESE_FEMALE_NAMES);
        const name = `${lastName} ${firstName}`;
        const email = `${toSlug(firstName)}.${toSlug(lastName)}.${startIndex + i}@email.com`;
        buyers.push({
            id: `buyer_gen_${startIndex + i}`,
            name: name,
            email: email,
            phone: `09${getRandomNumber(10000000, 99999999)}`
        });
    }
    return buyers;
};

const generateCompanies = (count: number): Company[] => {
    const companies: Company[] = [];
    for (let i = 0; i < count; i++) {
        const name = `${getRandomElement(COMPANY_PREFIXES)} ${getRandomElement(COMPANY_SUFFIXES)} ${i + 3}`;
        const email = `company_gen_${i + 3}@example.com`;
        companies.push({
            id: `company_gen_${i + 3}`,
            email: email,
            name: `Người đại diện ${i + 3}`,
            role: 'COMPANY',
            companyName: name,
            password: `password`
        });
    }
    return companies;
};

const generateArtists = (companies: Company[]): Artist[] => {
    const artists: Artist[] = [];
    let artistCount = 5;
    companies.forEach(company => {
        const numArtists = getRandomNumber(2, 5);
        for (let i = 0; i < numArtists; i++) {
            const isMale = Math.random() > 0.5;
            const lastName = getRandomElement(VIETNAMESE_LAST_NAMES);
            const firstName = isMale ? getRandomElement(VIETNAMESE_MALE_NAMES) : getRandomElement(VIETNAMESE_FEMALE_NAMES);
            const name = `${lastName} ${firstName}`;
            artists.push({
                id: `artist_gen_${artistCount++}`,
                name: name,
                companyId: company.id
            });
        }
    });
    return artists;
};

const generateShows = (count: number, allArtists: Artist[], allBuyers: Buyer[]): { shows: Show[], soldTickets: SoldTicket[] } => {
    const shows: Show[] = [];
    const soldTickets: SoldTicket[] = [];
    let ticketIdCounter = 10;
    
    for (let i = 0; i < count; i++) {
        const showId = `show_gen_${i + 4}`;
        const numArtists = getRandomNumber(1, 3);
        const artistIds: string[] = [];
        for (let j = 0; j < numArtists; j++) {
            artistIds.push(getRandomElement(allArtists).id);
        }

        const date = new Date();
        date.setDate(date.getDate() + getRandomNumber(-180, 180));
        date.setHours(getRandomNumber(19, 21));
        date.setMinutes(0);
        date.setSeconds(0);

        const ticketTypes: TicketType[] = [];
        const numTicketTypes = getRandomNumber(1, 4);
        for (let l = 0; l < numTicketTypes; l++) {
            const ticketTypeId = `ticket_gen_${ticketIdCounter++}`;
            const totalQuantity = getRandomNumber(50, 500);
            const soldQuantity = getRandomNumber(20, totalQuantity);
            ticketTypes.push({
                id: ticketTypeId,
                name: getRandomElement(['Thường', 'VIP', 'Pro VIP']),
                price: getRandomNumber(5, 50) * 100000,
                totalQuantity: totalQuantity,
                soldQuantity: soldQuantity
            });

            // Generate sold tickets for this type
            for (let m = 0; m < soldQuantity; m++) {
                soldTickets.push({
                    id: `sold_${showId}_${ticketTypeId}_${m + 1}`,
                    seatNumber: m + 1,
                    showId: showId,
                    ticketTypeId: ticketTypeId,
                    buyerId: getRandomElement(allBuyers).id,
                });
            }
        }
        
        shows.push({
            id: showId,
            name: `${getRandomElement(SHOW_PREFIXES)} ${getRandomElement(SHOW_THEMES)} ${i + 1}`,
            datetime: date.toISOString(),
            location: getRandomElement(LOCATIONS),
            artistIds: [...new Set(artistIds)], // Ensure unique artists
            ticketTypes: ticketTypes
        });
    }
    return { shows, soldTickets };
};


// --- INITIAL MOCK DATA ---
const INITIAL_USERS: User[] = [
  { id: 'admin1', email: 'admin@example.com', name: 'Admin User', role: 'ADMIN', password: 'admin' },
  { id: 'company1', email: 'company_a@example.com', name: 'Công ty A', role: 'COMPANY', companyName: 'Star Entertainment', password: 'company_a' },
  { id: 'company2', email: 'company_b@example.com', name: 'Công ty B', role: 'COMPANY', companyName: 'Galaxy Music', password: 'company_b' },
  { id: 'staff1', email: 'staff1@example.com', name: 'Nhân viên 1', role: 'STAFF', password: 'staff' },
  { id: 'staff2', email: 'staff2@example.com', name: 'Nhân viên 2', role: 'STAFF', password: 'staff' },
];

const INITIAL_ARTISTS: Artist[] = [
  { id: 'artist1', name: 'Sơn Tùng M-TP', companyId: 'company1' },
  { id: 'artist2', name: 'Hà Anh Tuấn', companyId: 'company1' },
  { id: 'artist3', name: 'Mỹ Tâm', companyId: 'company2' },
  { id: 'artist4', name: 'Đen Vâu', companyId: 'company2' },
];

const INITIAL_BUYERS: Buyer[] = [
    { id: 'buyer1', name: 'Nguyễn Văn A', email: 'nguyenvana@email.com', phone: '0912345678' },
    { id: 'buyer2', name: 'Trần Thị B', email: 'tranthib@email.com', phone: '0987654321' },
    { id: 'buyer3', name: 'Lê Văn C', email: 'levanc@email.com', phone: '0905123456' },
    { id: 'buyer4', name: 'Phạm Thị D', email: 'phamthid@email.com', phone: '0934567890' },
    { id: 'buyer5', name: 'Hoàng Văn E', email: 'hoangvane@email.com', phone: '0978111222' },
    { id: 'buyer6', name: 'Vũ Thị F', email: 'vuthif@email.com', phone: '0966333444' },
    { id: 'buyer7', name: 'Đặng Văn G', email: 'dangvang@email.com', phone: '0945777888' },
    { id: 'buyer8', name: 'Bùi Thị H', email: 'buithih@email.com', phone: '0915999000' },
];

const SOLD_TICKET_COUNTS = { ticket1: 80, ticket2: 150, ticket3: 120, ticket4: 60, ticket5: 90, ticket6: 180, ticket7: 300 };

const INITIAL_SOLD_TICKETS: SoldTicket[] = [
    ...Array.from({ length: SOLD_TICKET_COUNTS.ticket1 }, (_, i) => ({ id: `sold_show1_ticket1_${i+1}`, seatNumber: i + 1, showId: 'show1', ticketTypeId: 'ticket1', buyerId: INITIAL_BUYERS[i % INITIAL_BUYERS.length].id })),
    ...Array.from({ length: SOLD_TICKET_COUNTS.ticket2 }, (_, i) => ({ id: `sold_show1_ticket2_${i+1}`, seatNumber: i + 1, showId: 'show1', ticketTypeId: 'ticket2', buyerId: INITIAL_BUYERS[i % INITIAL_BUYERS.length].id })),
    ...Array.from({ length: SOLD_TICKET_COUNTS.ticket3 }, (_, i) => ({ id: `sold_show1_ticket3_${i+1}`, seatNumber: i + 1, showId: 'show1', ticketTypeId: 'ticket3', buyerId: INITIAL_BUYERS[i % INITIAL_BUYERS.length].id })),
    ...Array.from({ length: SOLD_TICKET_COUNTS.ticket4 }, (_, i) => ({ id: `sold_show2_ticket4_${i+1}`, seatNumber: i + 1, showId: 'show2', ticketTypeId: 'ticket4', buyerId: INITIAL_BUYERS[i % INITIAL_BUYERS.length].id })),
    ...Array.from({ length: SOLD_TICKET_COUNTS.ticket5 }, (_, i) => ({ id: `sold_show2_ticket5_${i+1}`, seatNumber: i + 1, showId: 'show2', ticketTypeId: 'ticket5', buyerId: INITIAL_BUYERS[i % INITIAL_BUYERS.length].id })),
    ...Array.from({ length: SOLD_TICKET_COUNTS.ticket6 }, (_, i) => ({ id: `sold_show3_ticket6_${i+1}`, seatNumber: i + 1, showId: 'show3', ticketTypeId: 'ticket6', buyerId: INITIAL_BUYERS[i % INITIAL_BUYERS.length].id })),
    ...Array.from({ length: SOLD_TICKET_COUNTS.ticket7 }, (_, i) => ({ id: `sold_show3_ticket7_${i+1}`, seatNumber: i + 1, showId: 'show3', ticketTypeId: 'ticket7', buyerId: INITIAL_BUYERS[i % INITIAL_BUYERS.length].id })),
];

const INITIAL_SHOWS: Show[] = [
  { id: 'show1', name: 'Sky Tour 2024', datetime: '2024-12-25T20:00:00', location: 'Sân vận động Mỹ Đình, Hà Nội', artistIds: ['artist1', 'artist4'], ticketTypes: [ { id: 'ticket1', name: 'VIP', price: 5000000, totalQuantity: 100, soldQuantity: SOLD_TICKET_COUNTS.ticket1 }, { id: 'ticket2', name: 'Thường', price: 2000000, totalQuantity: 500, soldQuantity: SOLD_TICKET_COUNTS.ticket2 }, { id: 'ticket3', name: 'Pro VIP', price: 3500000, totalQuantity: 200, soldQuantity: SOLD_TICKET_COUNTS.ticket3 } ]},
  { id: 'show2', name: 'Chân Trời Rực Rỡ', datetime: '2024-11-15T19:30:00', location: 'Nhà hát Hòa Bình, TP.HCM', artistIds: ['artist2', 'artist3'], ticketTypes: [ { id: 'ticket4', name: 'VIP', price: 4000000, totalQuantity: 150, soldQuantity: SOLD_TICKET_COUNTS.ticket4 }, { id: 'ticket5', name: 'Thường', price: 1500000, totalQuantity: 400, soldQuantity: SOLD_TICKET_COUNTS.ticket5 } ]},
  { id: 'show3', name: 'Concert Mùa Thu', datetime: '2023-09-10T20:00:00', location: 'Cung văn hóa hữu nghị Việt Xô, Hà Nội', artistIds: ['artist2'], ticketTypes: [ { id: 'ticket6', name: 'VIP', price: 2500000, totalQuantity: 200, soldQuantity: SOLD_TICKET_COUNTS.ticket6 }, { id: 'ticket7', name: 'Thường', price: 1000000, totalQuantity: 800, soldQuantity: SOLD_TICKET_COUNTS.ticket7 } ]}
];

// --- GENERATE AND COMBINE DATA ---
const GENERATED_COMPANIES = generateCompanies(40);
const GENERATED_ARTISTS = generateArtists(GENERATED_COMPANIES);
const GENERATED_BUYERS = generateBuyers(100, INITIAL_BUYERS.length + 1);

const ALL_ARTISTS = [...INITIAL_ARTISTS, ...GENERATED_ARTISTS];
const ALL_BUYERS = [...INITIAL_BUYERS, ...GENERATED_BUYERS];

const { shows: GENERATED_SHOWS, soldTickets: GENERATED_SOLD_TICKETS } = generateShows(100, ALL_ARTISTS, ALL_BUYERS);

export const MOCK_USERS: User[] = [...INITIAL_USERS, ...GENERATED_COMPANIES];
const MOCK_ARTISTS: Artist[] = ALL_ARTISTS;
const MOCK_SHOWS: Show[] = [...INITIAL_SHOWS, ...GENERATED_SHOWS];
const MOCK_BUYERS: Buyer[] = ALL_BUYERS;
const MOCK_SOLD_TICKETS: SoldTicket[] = [...INITIAL_SOLD_TICKETS, ...GENERATED_SOLD_TICKETS];

// --- DATA CONTEXT ---
interface DataContextType {
  users: User[];
  artists: Artist[];
  shows: Show[];
  companies: Company[];
  buyers: Buyer[];
  soldTickets: SoldTicket[];
  addArtist: (artist: Omit<Artist, 'id'>) => void;
  updateArtist: (artist: Artist) => void;
  deleteArtist: (artistId: string) => void;
  addUser: (user: Omit<User, 'id' | 'password'>, newArtists?: string[]) => void;
  updateUser: (user: Partial<User> & { id: string }) => void;
  deleteUser: (userId: string) => void;
  addShow: (show: Omit<Show, 'id'>) => void;
  updateShow: (show: Show) => void;
  deleteShow: (showId: string) => void;
}

const DataContext = createContext<DataContextType | undefined>(undefined);

export const DataProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [users, setUsers] = useState<User[]>(MOCK_USERS);
  const [artists, setArtists] = useState<Artist[]>(MOCK_ARTISTS);
  const [shows, setShows] = useState<Show[]>(MOCK_SHOWS);
  const [buyers, setBuyers] = useState<Buyer[]>(MOCK_BUYERS);
  const [soldTickets, setSoldTickets] = useState<SoldTicket[]>(MOCK_SOLD_TICKETS);
  
  const companies = users.filter(u => u.role === 'COMPANY') as Company[];

  const addArtist = (artist: Omit<Artist, 'id'>) => {
    const newArtist = { ...artist, id: `artist${Date.now()}` };
    setArtists(prev => [...prev, newArtist]);
  };

  const updateArtist = (updatedArtist: Artist) => {
    setArtists(prev => prev.map(artist => artist.id === updatedArtist.id ? updatedArtist : artist));
  };
  
  const deleteArtist = (artistId: string) => {
    setArtists(prev => prev.filter(artist => artist.id !== artistId));
  };

  const addUser = (user: Omit<User, 'id' | 'password'>, newArtists?: string[]) => {
    const newId = `user${Date.now()}`;
    const newUser = { ...user, id: newId, password: 'password' } as User;
    setUsers(prev => [...prev, newUser]);

    if (newArtists && newArtists.length > 0) {
        const artistsToAdd = newArtists.map((name, index) => ({
            id: `artist${Date.now()}${index}`,
            name,
            companyId: newId,
        }));
        setArtists(prev => [...prev, ...artistsToAdd]);
    }
  };

  const updateUser = (updatedUser: Partial<User> & { id: string }) => {
    setUsers(prev => prev.map(user => 
        // FIX: The object spread was creating a type that didn't match the `User` discriminated union.
        // Casting to `User` tells TypeScript we know the resulting object is valid.
        user.id === updatedUser.id ? { ...user, ...updatedUser } as User : user
    ));
  };

  const deleteUser = (userId: string) => {
    setUsers(prev => prev.filter(user => user.id !== userId));
  };

  const addShow = (show: Omit<Show, 'id'>) => {
    const newShow = { ...show, id: `show${Date.now()}` };
    setShows(prev => [...prev, newShow]);
  };

  const updateShow = (updatedShow: Show) => {
    setShows(prev => prev.map(show => show.id === updatedShow.id ? updatedShow : show));
  };

  const deleteShow = (showId: string) => {
    setShows(prev => prev.filter(show => show.id !== showId));
  };

  return (
    <DataContext.Provider value={{ 
        users, artists, shows, companies, buyers, soldTickets,
        addArtist, updateArtist, deleteArtist,
        addUser, updateUser, deleteUser,
        addShow, updateShow, deleteShow
    }}>
      {children}
    </DataContext.Provider>
  );
};

export const useData = () => {
  const context = useContext(DataContext);
  if (context === undefined) {
    throw new Error('useData must be used within a DataProvider');
  }
  return context;
};