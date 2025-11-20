export type UserRole = 'ADMIN' | 'COMPANY' | 'STAFF';

export interface BaseUser {
  id: string;
  email: string;
  name: string;
  role: UserRole;
  password: string;
}

export interface Company extends BaseUser {
  role: 'COMPANY';
  companyName: string;
}

export interface Staff extends BaseUser {
  role: 'STAFF';
}

export type User = Company | Staff | (BaseUser & { role: 'ADMIN' });

export interface Artist {
  id: string;
  name: string;
  companyId: string;
}

export interface TicketType {
  id: string;
  name: 'VIP' | 'Thường' | 'Pro VIP';
  price: number;
  totalQuantity: number;
  soldQuantity: number;
}

export interface Show {
  id: string;
  name: string;
  datetime: string;
  location: string;
  artistIds: string[];
  ticketTypes: TicketType[];
}

export interface Buyer {
  id: string;
  name: string;
  email: string;
  phone: string;
}

export interface SoldTicket {
  id: string;
  seatNumber: number;
  showId: string;
  ticketTypeId: string;
  buyerId: string;
}