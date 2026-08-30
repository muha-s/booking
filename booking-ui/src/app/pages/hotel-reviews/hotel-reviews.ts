import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Location } from '@angular/common';
import { HotelReviewService } from '../../services/hotel-review';
import { HotelReviewForHotel } from '../../models/hotel-review-for-hotel';

@Component({
  selector: 'app-hotel-reviews',
  imports: [RouterLink],
  templateUrl: './hotel-reviews.html',
  styleUrl: './hotel-reviews.css'
})
export class HotelReviews implements OnInit {

  reviews = signal<HotelReviewForHotel[]>([]);
  errorMessage = signal('');

  constructor(
    private readonly hotelReviewService: HotelReviewService,
    private readonly route: ActivatedRoute,
    private readonly location: Location
  ) {
  }

  ngOnInit(): void {

    const hotelId = Number(
      this.route.snapshot.paramMap.get('hotelId')
    );

    if (!hotelId) {
      this.errorMessage.set(
        'Некорректный идентификатор отеля'
      );
      return;
    }

    this.hotelReviewService.findCommentsByHotelId(hotelId)
      .subscribe({
        next: reviews => {
          this.reviews.set(reviews);
        },

        error: error => {
          this.errorMessage.set(
            error.error?.message
              ?? 'Не удалось загрузить отзывы'
          );
        }
      });
  }

  goBack(): void {
    this.location.back();
  }
}
