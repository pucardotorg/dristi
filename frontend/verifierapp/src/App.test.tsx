import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';
import { MemoryRouter } from 'react-router-dom';

describe('App Component', () => {
  test('renders App component without crashing', () => {
    render(
      <MemoryRouter initialEntries={['/']}>
        <App />
      </MemoryRouter>
    );
    // Adjust the expected text based on actual content
    expect(screen.getByText("/Home/i")).toBeInTheDocument();
  });

  test('navigates to home page correctly', () => {
    render(
      <MemoryRouter initialEntries={['/']}>
        <App />
      </MemoryRouter>
    );
    // Adjust the expected text based on actual content
    expect(screen.getByText("/Home/i")).toBeInTheDocument();
  });

  test('navigates to offline page correctly', () => {
    render(
      <MemoryRouter initialEntries={['/offline']}>
        <App />
      </MemoryRouter>
    );
    // Adjust the expected text based on actual content
    expect(screen.getByText("/Something Went Wrong/i")).toBeInTheDocument();
  });
});