import { IEmployee } from 'app/shared/model/employee.model';
import { Pays } from 'app/shared/model/enumerations/pays.model';

export interface IEnterprise {
  id?: number;
  companyName?: string;
  businessRegisterNumber?: string;
  uniqueIdentificationNumber?: string;
  businessDomicile?: string | null;
  email?: string;
  businessPhone?: string;
  country?: keyof typeof Pays | null;
  city?: string | null;
  businessLogoContentType?: string | null;
  businessLogo?: string | null;
  mapLocatorContentType?: string | null;
  mapLocator?: string | null;
  employee?: IEmployee | null;
}

export const defaultValue: Readonly<IEnterprise> = {};
